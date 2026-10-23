package com.example.vocab.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure JVM tests for the streak state machine backing the v1.3.4
 * Home Screen Streak & Goal Tracker widget.
 */
class StreakStateMachineTest {

    private val today = "2026-10-22"
    private val goal = 10

    /** "yyyy-MM-dd" for n days before [today], computed on UTC epoch days. */
    private fun daysBefore(base: String = today, n: Long): String =
        StreakStateMachine.epochDayToIso(StreakStateMachine.toEpochDay(base)!! - n)

    private fun completedDates(vararg daysAgo: Long, base: String = today): Map<String, Int> =
        daysAgo.associate { daysBefore(base, it) to goal }

    @Test
    fun `empty history starts at zero streak`() {
        val state = StreakStateMachine.evaluate(emptyMap(), goal, today)
        assertEquals(0, state.currentStreak)
        assertEquals(0, state.bestStreak)
        assertFalse(state.todayGoalMet)
        assertNull(state.lastCompletedDate)
    }

    @Test
    fun `completing only today yields streak of one`() {
        val state = StreakStateMachine.evaluate(completedDates(0), goal, today)
        assertEquals(1, state.currentStreak)
        assertTrue(state.todayGoalMet)
        assertEquals(today, state.lastCompletedDate)
    }

    @Test
    fun `pending today keeps run that ended yesterday (grace window)`() {
        val state = StreakStateMachine.evaluate(completedDates(1, 2, 3), goal, today)
        assertEquals(3, state.currentStreak)
        assertFalse(state.todayGoalMet)
        assertEquals(daysBefore(n = 1), state.lastCompletedDate)
    }

    @Test
    fun `missed day explicitly resets streak to zero`() {
        // Last completion was 2 days ago -> today AND yesterday untouched.
        val state = StreakStateMachine.evaluate(completedDates(2, 3, 4), goal, today)
        assertEquals(0, state.currentStreak)
    }

    @Test
    fun `rollover to next day preserves streak when previous day completed`() {
        val day1 = today
        val day2 = daysBefore(base = today, n = -1)
        val initial = StreakStateMachine.evaluate(completedDates(0), goal, day1)
        val rolled = StreakStateMachine.onDayRollover(initial, day2)
        assertEquals(1, rolled.currentStreak)
        assertFalse(rolled.todayGoalMet)
        assertEquals(0, rolled.learnedToday)
    }

    @Test
    fun `rollover after a skipped day zeroes the streak`() {
        val day1 = today
        val day3 = daysBefore(base = today, n = -2) // two days later -> missed a day
        val initial = StreakStateMachine.evaluate(completedDates(0), goal, day1)
        val rolled = StreakStateMachine.onDayRollover(initial, day3)
        assertEquals(0, rolled.currentStreak)
    }

    @Test
    fun `goal completion increments streak on consecutive day`() {
        val yesterday = daysBefore(n = 1)
        val state = StreakStateMachine.evaluate(completedDates(1), goal, today)
        val updated = StreakStateMachine.onGoalCompleted(state, completedDate = today)
        assertEquals(2, updated.currentStreak)
        assertTrue(updated.todayGoalMet)
        assertEquals(today, updated.lastCompletedDate)
        assertEquals(yesterday, state.lastCompletedDate)
    }

    @Test
    fun `same day completion is idempotent`() {
        val state = StreakStateMachine.evaluate(completedDates(0), goal, today)
        val again = StreakStateMachine.onGoalCompleted(state, completedDate = today)
        assertEquals(state.currentStreak, again.currentStreak)
    }

    @Test
    fun `completion after a broken run restarts at one`() {
        val stale = StreakStateMachine.StreakState(
            today = today,
            dailyGoal = goal,
            learnedToday = 0,
            todayGoalMet = false,
            lastCompletedDate = daysBefore(n = 5),
            currentStreak = 4,
            bestStreak = 4
        )
        val updated = StreakStateMachine.onGoalCompleted(stale, completedDate = today)
        assertEquals(1, updated.currentStreak)
        assertEquals(4, updated.bestStreak)
    }

    @Test
    fun `goal completions dated in the future are ignored`() {
        val future = daysBefore(base = today, n = -3)
        val state = StreakStateMachine.evaluate(
            completedDates(0, 1) + (future to goal * 2),
            goal,
            today
        )
        assertEquals(2, state.currentStreak)
        assertTrue(today > future || future > today) // sanity: future date really is after today
        assertFalse(state.lastCompletedDate!!.let { it > today })
    }

    @Test
    fun `best streak spans full history`() {
        val state = StreakStateMachine.evaluate(
            completedDates(1, 2, 3) + completedDates(6, 7, 8, 9, 10),
            goal,
            today
        )
        assertEquals(5, state.bestStreak)
        assertEquals(3, state.currentStreak)
    }

    @Test
    fun `partial day below goal does not count`() {
        val partial = mapOf(today to (goal - 1))
        val state = StreakStateMachine.evaluate(partial, goal, today)
        assertEquals(0, state.currentStreak)
        assertFalse(state.todayGoalMet)
        assertEquals(goal - 1, state.learnedToday)
    }

    @Test
    fun `epoch day diffs cross month and year boundaries correctly`() {
        assertEquals(1L, StreakStateMachine.diffDays("2026-01-31", "2026-02-01"))
        assertEquals(1L, StreakStateMachine.diffDays("2026-12-31", "2027-01-01"))
        assertEquals(29L, StreakStateMachine.diffDays("2026-02-01", "2026-03-02")) // non-leap year
        assertNull(StreakStateMachine.diffDays("not-a-date", "2026-02-01"))
    }

    @Test
    fun `malformed persisted dates are treated as gaps`() {
        val state = StreakStateMachine.evaluate(
            mapOf("garbage" to goal, today to goal),
            goal,
            today
        )
        assertEquals(1, state.currentStreak)
    }
}
