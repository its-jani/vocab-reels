package com.example.vocab.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DailyGoalDialog(
    currentGoal: Int,
    learnedToday: Int,
    onDismiss: () -> Unit,
    onSaveGoal: (Int) -> Unit
) {
    var selectedGoal by remember { mutableIntStateOf(currentGoal) }
    val goalOptions = listOf(5, 10, 15, 20, 25, 30)

    val progress = if (selectedGoal > 0) (learnedToday.toFloat() / selectedGoal).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "dialog_progress_anim"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Brush.linearGradient(listOf(ElectricIndigo, NeonCyan)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Daily Learning Goal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LightText
                    )
                    Text(
                        text = "Track your daily vocabulary habit",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedText
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Progress Ring with Stats
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(120.dp),
                        color = DeepSurfaceVariant,
                        strokeWidth = 10.dp,
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(120.dp),
                        color = if (learnedToday >= selectedGoal) EmeraldGreen else NeonCyan,
                        strokeWidth = 10.dp,
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$learnedToday / $selectedGoal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = LightText
                        )
                        Text(
                            text = if (learnedToday >= selectedGoal) "Goal Met! 🎉" else "${(progress * 100).toInt()}% Done",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (learnedToday >= selectedGoal) EmeraldGreen else NeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Select your daily target:",
                    style = MaterialTheme.typography.labelMedium,
                    color = SubtitleText,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Goal option chips grid (2 rows of 3)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goalOptions.take(3).forEach { target ->
                            GoalChip(
                                target = target,
                                isSelected = selectedGoal == target,
                                onClick = { selectedGoal = target },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goalOptions.drop(3).forEach { target ->
                            GoalChip(
                                target = target,
                                isSelected = selectedGoal == target,
                                onClick = { selectedGoal = target },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tip: Words count towards today's goal only when you mark them as learned.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MutedText,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveGoal(selectedGoal) },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_daily_goal_button")
            ) {
                Text("Set Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedText)
            }
        },
        containerColor = DeepSurface,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun GoalChip(
    target: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) ElectricIndigo.copy(alpha = 0.3f) else DeepSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) NeonCyan else CardBorder
        ),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("goal_chip_$target")
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$target",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) NeonCyan else LightText
            )
            Text(
                text = "words",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) LightText else MutedText,
                fontSize = 10.sp
            )
        }
    }
}
