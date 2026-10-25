package com.example.vocab.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
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
 * Resizable: the card freely scales horizontally and vertically (see
 * res/xml/streak_widget_info.xml). On every resize the system re-runs
 * [provideGlance], which reads the widget's live [DpSize] and derives a uniform
 * scale factor so the flame glyph, streak number, status pill and hint row grow
 * and shrink together — and the hint row is dropped when the card gets too
 * short — instead of overflowing or leaving dead space.
 *
 * Refresh paths:
 * - App data changes (goal met toggles, goal edits) -> [StreakWidgetRefresher].
 * - Resize -> system re-invokes [provideGlance] with the new size.
 * - Midnight rollover / missed days -> system periodic refresh (30 min, the
 *   platform maximum, see res/xml/streak_widget_info.xml) plus widget rebind on
 *   device boot.
 */
class StreakWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = StreakWidgetData.loadState(context)
        val size = currentSize(context, id)

        val openTodayIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(MainActivity.EXTRA_OPEN_TODAYS_ACTIVITY, true)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        provideContent {
            StreakWidgetContent(
                state = state,
                onCardClick = actionStartActivity(openTodayIntent),
                size = size
            )
        }
    }

    /**
     * Live size of this widget instance in dp. Falls back to the default card
     * footprint (the 3x2 target cell) if the manager cannot report one, e.g.
     * during an early first-frame render before options are populated.
     */
    private suspend fun currentSize(context: Context, id: GlanceId): DpSize {
        val fallback = DpSize(250.dp, 110.dp)
        val sizes = runCatching { GlanceAppWidgetManager(context).getAppWidgetSizes(id) }
            .getOrDefault(emptyList())
        return sizes.maxByOrNull { it.width.value * it.height.value } ?: fallback
    }
}

/** Reference footprint the layout was designed for; scale is relative to it. */
private const val BASE_WIDTH_DP = 250f
private const val BASE_HEIGHT_DP = 110f

/**
 * Widget card layout: streak counter, today's goal status pill, and a hint row.
 * The whole card is the quick-action target (one tap -> today's activity).
 *
 * Every dimension is multiplied by a uniform [scale] derived from the current
 * widget size, so the content tracks the user's resize while the `min` ratio
 * guarantees nothing overflows the card.
 */
@Composable
private fun StreakWidgetContent(
    state: StreakStateMachine.StreakState,
    onCardClick: Action,
    size: DpSize,
) {
    val scale = minOf(size.width.value / BASE_WIDTH_DP, size.height.value / BASE_HEIGHT_DP)
        .coerceIn(0.75f, 2.2f)
    // The hint row only fits once the card is tall enough to carry it.
    val showHint = size.height.value >= 100f

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
            .cornerRadius((18 * scale).dp)
            .clickable(onClick = onCardClick)
            .padding((16 * scale).dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥", style = TextStyle(fontSize = (26 * scale).sp))
            Spacer(modifier = GlanceModifier.width((8 * scale).dp))
            Text(
                text = state.currentStreak.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = (34 * scale).sp,
                    color = ColorProvider(AmberOrange)
                )
            )
            Spacer(modifier = GlanceModifier.width((8 * scale).dp))
            Text(
                text = if (state.currentStreak == 1) "day\nstreak" else "days\nstreak",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = (13 * scale).sp,
                    color = ColorProvider(MutedText)
                )
            )
        }

        Spacer(modifier = GlanceModifier.height((10 * scale).dp))

        Text(
            text = statusLabel,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = (14 * scale).sp,
                color = ColorProvider(statusColor)
            )
        )

        if (showHint) {
            Spacer(modifier = GlanceModifier.height((6 * scale).dp))

            Text(
                text = "Tap to continue today's activity",
                style = TextStyle(fontSize = (11 * scale).sp, color = ColorProvider(MutedText)),
                modifier = GlanceModifier.fillMaxWidth()
            )
        }
    }
}
