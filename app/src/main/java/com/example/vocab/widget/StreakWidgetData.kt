package com.example.vocab.widget

import android.content.Context
import com.example.vocab.data.local.VocabDatabase
import com.example.vocab.util.StreakStateMachine

/**
 * One-shot read model over the Room database (the app's persistent shared store)
 * for rendering the home screen widget outside of the app process lifecycle.
 * Flow-based repository APIs are not usable from a short-lived widget refresh,
 * so this helper reuses the suspend snapshot queries added to VocabDao.
 */
internal object StreakWidgetData {

    suspend fun loadState(context: Context): StreakStateMachine.StreakState {
        val dao = VocabDatabase.getDatabase(context.applicationContext).vocabDao()
        val stats = dao.getDailyStatsHistorySnapshot()
        val goal = dao.getLearningGoalSnapshot()?.dailyGoal
            ?: StreakStateMachine.DEFAULT_DAILY_GOAL
        return StreakStateMachine.evaluate(
            learnedByDate = stats.associate { it.date to it.wordsLearned },
            dailyGoal = goal
        )
    }
}
