package com.example.vocab.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.compose.ui.graphics.Color
import com.example.MainActivity
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.DeepBackground
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LightText
import com.example.ui.theme.MutedText
import com.example.vocab.util.StreakStateMachine

/**
 * Home Screen Streak & Goal Tracker Widget (v1.3.4).
 *
 * Renders, in real time:
 * - Current streak (consecutive days the daily goal was completed).
 * - Today's goal status: Completed / Pending (learned words vs. goal).
 * - Auto-reset semantics come from [StreakStateMachine.evaluate]: a missed day
 *   drops the streak to 0. Day math uses UTC epoch-day boundaries derived from
 *   local calendar dates, so timezone / DST shifts cannot spoof the streak.
 *
 * Quick action: tapping anywhere on the card launches [MainActivity] with
 * [MainActivity.EXTRA_OPEN_TODAYS_ACTIVITY], jumping straight to today's
 * activity panel.
 *
 * Refresh paths:
 * - App data changes (goal met toggles, goal edits) -> [StreakWidgetRefresher].
 * - Midnight rollover / missed days -> system periodic refresh (30 min, see
 *   res/xml/streak_widget_info.xml) plus widget rebind on device boot.
 */
class StreakWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = StreakWidgetData.loadState(context)

        val openTodayIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(MainActivity.EXTRA_OPEN_TODAYS_ACTIVITY, true)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        provideContent {
            StreakWidgetContent(
                state = state,
                onCardClick = actionStartActivity(openTodayIntent)
            )
        }
    }
}

/**
 * Widget card layout: streak counter, today's goal status pill, and a hint row.
 * The whole card is the quick-action target (one tap -> today's activity).
 */
@Composable
private fun StreakWidgetContent(
    state: StreakStateMachine.StreakState,
    onCardClick: Action
) {
    val statusLabel: String
    val statusColor: Color
    if (state.todayGoalMet) {
        statusLabel = "Today's goal completed ✓"
        statusColor = EmeraldGreen
    } else {
        statusLabel = "Pending · ${state.learnedToday}/${state.dailyGoal} words today"
        statusColor = AmberOrange
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(DeepBackground))
            .cornerRadius(20.dp)
            .clickable(onClick = onCardClick)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥", style = TextStyle(fontSize = 26.sp))
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = state.currentStreak.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    color = ColorProvider(AmberOrange)
                )
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = if (state.currentStreak == 1) "day\nstreak" else "days\nstreak",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = ColorProvider(MutedText)
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(10.dp))

        Text(
            text = statusLabel,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = ColorProvider(statusColor)
            )
        )

        Spacer(modifier = GlanceModifier.height(6.dp))

        Text(
            text = "Tap to continue today's activity",
            style = TextStyle(fontSize = 11.sp, color = ColorProvider(MutedText)),
            modifier = GlanceModifier.fillMaxWidth()
        )
    }
}
