package com.example.vocab.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Pushes a real-time widget refresh whenever persisted streak/goal data changes
 * (e.g. a word is marked learned or the daily goal is edited).
 *
 * The refresh is fire-and-forget and cheap: Glance re-runs
 * [StreakWidget.provideGlance] off the main process path on its own dispatcher.
 * Failures (e.g. widget removed mid-refresh) are swallowed — the widget simply
 * stays on its last rendered state until the next scheduled update.
 */
object StreakWidgetRefresher {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun refreshAll(context: Context) {
        val appContext = context.applicationContext
        scope.launch {
            runCatching {
                StreakWidget().updateAll(appContext)
            }
        }
    }
}
