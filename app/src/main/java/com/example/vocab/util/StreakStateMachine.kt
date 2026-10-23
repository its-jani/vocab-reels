package com.example.vocab.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Pure, persistence-agnostic streak state machine.
 *
 * This is the single source of truth for streak semantics, shared by the
 * in-app dashboard ([com.example.vocab.ui.VocabViewModel]) and the home
 * screen Streak & Goal Tracker widget ([com.example.vocab.widget.StreakWidget]):
 *
 * - A day counts as "completed" when `wordsLearned >= dailyGoal`.
 * - Completing today's goal increments the streak (or starts it at 1).
 * - While today is still pending, the streak keeps showing the run that ended
 *   yesterday (grace window until local midnight).
 * - If a full day is missed, the streak explicitly drops to 0 (zero-reset).
 * - All day arithmetic is performed on UTC epoch-day boundaries derived from
 *   LOCAL calendar dates ("yyyy-MM-dd"), which makes increment / reset logic
 *   immune to timezone changes and DST short/long days. The machine never
 *   trusts `updatedAt` timestamps directly.
 * - Goal completions dated in the future are ignored (anti-spoofing).
 *
 * Event-driven transitions ([onGoalCompleted] / [onDayRollover]) are provided
 * for incremental use, while [evaluate] derives the full state from the
 * persisted daily history (used after process death or widget refresh).
 */
object StreakStateMachine {

    const val DEFAULT_DAILY_GOAL = 10

    /** Immutable snapshot of the streak state at [today]. */
    data class StreakState(
        val today: String,                       // local "yyyy-MM-dd"
        val dailyGoal: Int,
        val learnedToday: Int,
        val todayGoalMet: Boolean,
        val lastCompletedDate: String?,          // last local date whose goal was met
        val currentStreak: Int,
        val bestStreak: Int
    ) {
        companion object {
            fun empty(today: String, dailyGoal: Int): StreakState = StreakState(
                today = today,
                dailyGoal = dailyGoal,
                learnedToday = 0,
                todayGoalMet = false,
                lastCompletedDate = null,
                currentStreak = 0,
                bestStreak = 0
            )
        }
    }

    /** Local calendar date as "yyyy-MM-dd" — matches the persisted DailyStats keys. */
    fun localTodayIso(nowMillis: Long = System.currentTimeMillis()): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(nowMillis))

    /**
     * Converts a "yyyy-MM-dd" local calendar date to its UTC epoch day number.
     * Strictly parsed in UTC so the value is a pure calendar-day index,
     * independent of device timezone and DST transitions.
     * Returns null for malformed dates (which are then treated as gaps).
     */
    fun toEpochDay(isoDate: String): Long? = try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            isLenient = false
        }
        val millis = format.parse(isoDate)?.time ?: return null
        TimeUnit.MILLISECONDS.toDays(millis)
    } catch (t: Throwable) {
        null
    }

    /** Whole calendar days between two "yyyy-MM-dd" dates ([to] minus [from]); null if unparseable. */
    fun diffDays(fromIso: String, toIso: String): Long? {
        val from = toEpochDay(fromIso) ?: return null
        val to = toEpochDay(toIso) ?: return null
        return to - from
    }

    /**
     * Derives the complete streak state from the persisted history of
     * learned-word counts per local date (date "yyyy-MM-dd" -> wordsLearned).
     */
    fun evaluate(
        learnedByDate: Map<String, Int>,
        dailyGoal: Int,
        today: String = localTodayIso()
    ): StreakState {
        val goal = dailyGoal.coerceAtLeast(1)
        val todayDay = toEpochDay(today) ?: return StreakState.empty(today, goal)

        val learnedToday = learnedByDate[today]?.coerceAtLeast(0) ?: 0
        val todayGoalMet = learnedToday >= goal

        // Completed days, excluding anything dated in the future (spoof guard).
        val metDays: Set<Long> = learnedByDate
            .filter { (_, learned) -> learned >= goal }
            .keys
            .mapNotNull { toEpochDay(it) }
            .filter { it <= todayDay }
            .toSet()

        val lastCompletedDay = metDays.maxOrNull()

        val currentStreak = when {
            lastCompletedDay == null -> 0
            // Zero-reset: a day was missed between the last completion and today.
            todayDay - lastCompletedDay > 1L -> 0
            else -> {
                // Walk backwards across consecutive epoch days.
                var cursor = lastCompletedDay
                var count = 0
                while (cursor in metDays) {
                    count++
                    cursor--
                }
                count
            }
        }

        return StreakState(
            today = today,
            dailyGoal = goal,
            learnedToday = learnedToday,
            todayGoalMet = todayGoalMet,
            lastCompletedDate = lastCompletedDay?.let { epochDayToIso(it) },
            currentStreak = currentStreak,
            bestStreak = bestStreakOf(metDays)
        )
    }

    /**
     * Transition: the goal for [completedDate] was completed.
     * Increments the streak when [completedDate] directly follows the previous
     * completion, starts (or restarts) it at 1 otherwise. Idempotent per day.
     * Feed dates in non-decreasing order.
     */
    fun onGoalCompleted(state: StreakState, completedDate: String = state.today): StreakState {
        val last = state.lastCompletedDate
        val streak = when {
            last == completedDate -> state.currentStreak.coerceAtLeast(1)   // same-day repeat
            last == null -> 1
            diffDays(last, completedDate) == 1L -> state.currentStreak + 1  // increment
            else -> 1                                                       // run was broken; restart
        }
        val lastCompleted =
            if (last != null && (toEpochDay(last) ?: Long.MIN_VALUE) >= (toEpochDay(completedDate) ?: Long.MIN_VALUE)) {
                last
            } else {
                completedDate
            }
        return state.copy(
            todayGoalMet = if (completedDate == state.today) true else state.todayGoalMet,
            learnedToday = if (completedDate == state.today) maxOf(state.learnedToday, state.dailyGoal) else state.learnedToday,
            lastCompletedDate = lastCompleted,
            currentStreak = streak,
            bestStreak = maxOf(state.bestStreak, streak)
        )
    }

    /**
     * Transition: the local date rolled over to [newToday].
     * Applies the explicit zero-reset when a day was missed, and clears the
     * pending "today" progress so each day starts fresh at midnight.
     */
    fun onDayRollover(state: StreakState, newToday: String): StreakState {
        if (newToday == state.today) return state
        val gap = state.lastCompletedDate?.let { diffDays(it, newToday) }
        val dayMissed = gap == null || gap > 1L
        return state.copy(
            today = newToday,
            learnedToday = 0,
            todayGoalMet = false,
            currentStreak = if (dayMissed) 0 else state.currentStreak
        )
    }

    /** Longest run of consecutive completed days in the history. */
    fun bestStreakOf(metDays: Set<Long>): Int {
        if (metDays.isEmpty()) return 0
        var best = 1
        var run = 1
        var previous: Long? = null
        for (day in metDays.sorted()) {
            run = if (previous != null && day - previous == 1L) run + 1 else 1
            if (run > best) best = run
            previous = day
        }
        return best
    }

    /** Formats a UTC epoch day back to a "yyyy-MM-dd" local-style date string. */
    fun epochDayToIso(epochDay: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return format.format(Date(TimeUnit.DAYS.toMillis(epochDay)))
    }
}
