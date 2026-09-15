package com.example.vocab.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DailyProgressRingWidget(
    learnedToday: Int,
    dailyGoal: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (dailyGoal > 0) (learnedToday.toFloat() / dailyGoal).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ring_widget_anim"
    )
    val isGoalAchieved = learnedToday >= dailyGoal && dailyGoal > 0

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DeepSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isGoalAchieved) EmeraldGreen.copy(alpha = 0.6f) else CardBorder
        ),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("daily_goal_ring_widget")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(22.dp),
                    color = CardBorder,
                    strokeWidth = 3.dp,
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(22.dp),
                    color = if (isGoalAchieved) EmeraldGreen else NeonCyan,
                    strokeWidth = 3.dp,
                )
                if (isGoalAchieved) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Goal reached",
                        tint = AmberOrange,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "$learnedToday/$dailyGoal",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isGoalAchieved) EmeraldGreen else LightText
                )
                Text(
                    text = if (isGoalAchieved) "Done!" else "Today",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = MutedText
                )
            }
        }
    }
}

@Composable
fun DailyProgressBarBanner(
    learnedToday: Int,
    dailyGoal: Int,
    onOpenGoalDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (dailyGoal > 0) (learnedToday.toFloat() / dailyGoal).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "banner_progress_anim"
    )
    val isGoalAchieved = learnedToday >= dailyGoal && dailyGoal > 0

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DeepSurface.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isGoalAchieved) EmeraldGreen.copy(alpha = 0.5f) else CardBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenGoalDialog)
            .testTag("daily_progress_bar_banner")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isGoalAchieved) Icons.Default.EmojiEvents else Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = if (isGoalAchieved) AmberOrange else NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isGoalAchieved) "Daily Goal Completed! 🎉" else "Today's Vocabulary Goal",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isGoalAchieved) EmeraldGreen else LightText
                    )
                }

                Text(
                        text = "$learnedToday / $dailyGoal learned (${(progress * 100).toInt()}%)",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonCyan
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Linear Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DeepSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                if (isGoalAchieved) listOf(EmeraldGreen, NeonCyan) else listOf(ElectricIndigo, NeonCyan)
                            )
                        )
                )
            }
        }
    }
}
