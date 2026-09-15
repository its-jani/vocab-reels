package com.example.vocab.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.vocab.ui.CategoryProgressMetric

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuBottomSheet(
    onDismiss: () -> Unit,
    reviewsScrolledToday: Int,
    wordsLearnedToday: Int,
    wordsLearnedYesterday: Int,
    streakDays: Int,
    bestStreakDays: Int,
    consistencyPercent: Int,
    dailyGoal: Int,
    categoryMetrics: List<CategoryProgressMetric>,
    onSelectCategory: (String) -> Unit,
    onOpenLikedWords: () -> Unit,
    onOpenLearnedWords: () -> Unit,
    onOpenGoalDialog: () -> Unit,
    onReshuffleFeed: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DeepBackground,
        dragHandle = null,
        modifier = modifier.statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header: Title + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(DeepSurfaceVariant, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Learning Dashboard",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LightText
                        )
                        Text(
                            text = "Daily Stats & Category Mastery",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_main_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Menu",
                        tint = MutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Today's Activity & Progress
                item {
                    Text(
                        text = "TODAY'S ACTIVITY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Reviews Scrolled Today Card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepSurface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorder, ElectricIndigo.copy(alpha = 0.5f)))),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = ElectricIndigo,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Scrolled Today",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SubtitleText
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$reviewsScrolledToday",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = LightText
                                )
                                Text(
                                    text = "Reviews completed",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Words Learned Today Card + Comparison vs Yesterday
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepSurface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorder, EmeraldGreen.copy(alpha = 0.5f)))),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Learned Today",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SubtitleText
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$wordsLearnedToday",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldGreen
                                )

                                val diff = wordsLearnedToday - wordsLearnedYesterday
                                val comparisonText = when {
                                    wordsLearnedYesterday == 0 && wordsLearnedToday > 0 -> "First today!"
                                    diff > 0 -> "▲ +$diff vs yesterday"
                                    diff < 0 -> "▼ ${-diff} vs yesterday"
                                    else -> "= Same as yesterday ($wordsLearnedYesterday)"
                                }
                                val comparisonColor = when {
                                    diff > 0 -> EmeraldGreen
                                    diff < 0 -> AmberOrange
                                    else -> NeonCyan
                                }
                                Text(
                                    text = comparisonText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = comparisonColor,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Section 2: Streak & Overall Consistency
                item {
                    Text(
                        text = "CONSISTENCY & GOAL TRACKING",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberOrange,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = DeepSurface),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorder, AmberOrange.copy(alpha = 0.4f)))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Streak Counter Box
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(AmberOrange.copy(alpha = 0.2f), CircleShape)
                                            .border(1.dp, AmberOrange.copy(alpha = 0.6f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = "Streak",
                                            tint = AmberOrange,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "$streakDays Day Streak",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = LightText
                                        )
                                        Text(
                                            text = "Best: $bestStreakDays consecutive days",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MutedText
                                        )
                                    }
                                }

                                // Consistency Rate Badge
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = DeepSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "$consistencyPercent%",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Black,
                                            color = NeonCyan
                                        )
                                        Text(
                                            text = "Consistency",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = MutedText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = CardBorder.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Today's Goal Progress Bar
                            val goalProgress = if (dailyGoal > 0) (wordsLearnedToday.toFloat() / dailyGoal).coerceIn(0f, 1f) else 0f
                            val isGoalDone = wordsLearnedToday >= dailyGoal && dailyGoal > 0

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isGoalDone) "Today's Goal Completed! 🎯" else "Daily Goal Progress",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGoalDone) EmeraldGreen else LightText
                                )
                                Text(
                                    text = "$wordsLearnedToday / $dailyGoal learned",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isGoalDone) EmeraldGreen else NeonCyan
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { goalProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (isGoalDone) EmeraldGreen else NeonCyan,
                                trackColor = DeepSurfaceVariant,
                            )
                        }
                    }
                }

                // Section 3: Category Progress & Remaining Words
                item {
                    Text(
                        text = "CATEGORY PROGRESS & REMAINING CONTENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricIndigo,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(categoryMetrics, key = { it.category }) { metric ->
                    val categoryColor = CategoryBadgeHelper.getCategoryColor(metric.category)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = DeepSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectCategory(metric.category)
                                onDismiss()
                            }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(categoryColor, CircleShape)
                                    )
                                    Text(
                                        text = metric.category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = LightText
                                    )
                                }

                                Text(
                                    text = "${metric.percentCompleted}% Completed",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = categoryColor
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { metric.percentCompleted / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = categoryColor,
                                trackColor = DeepSurfaceVariant,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "✓ ${metric.learnedWords} learned",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EmeraldGreen,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "♥ ${metric.likedWords} liked",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CoralPink,
                                        fontSize = 11.sp
                                    )
                                }

                                Text(
                                    text = "${metric.remainingWords} remaining of ${metric.totalWords}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Section 4: Quick Actions
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "QUICK ACTIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedText,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DeepSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onDismiss()
                                    onOpenLikedWords()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = CoralPink, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Liked Words", style = MaterialTheme.typography.labelMedium, color = LightText)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DeepSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onDismiss()
                                    onOpenLearnedWords()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Learned Words", style = MaterialTheme.typography.labelMedium, color = LightText)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DeepSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onDismiss()
                                    onOpenGoalDialog()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.TrackChanges, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Set Daily Goal", style = MaterialTheme.typography.labelMedium, color = LightText)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DeepSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onReshuffleFeed()
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Shuffle, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Shuffle Feed", style = MaterialTheme.typography.labelMedium, color = LightText)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
