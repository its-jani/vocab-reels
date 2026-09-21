package com.example.vocab.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabWord

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReelCard(
    word: VocabWord,
    currentIndex: Int,
    totalCount: Int,
    isSpeaking: Boolean,
    currentSpeechRate: Float,
    onPronounce: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleLearned: () -> Unit,
    onSetConfidence: (Int) -> Unit,
    onSetSpeechRate: (Float) -> Unit,
    onPreviousWord: () -> Unit,
    onNextWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categoryColor = CategoryBadgeHelper.getCategoryColor(word.category)
    val difficultyColor = CategoryBadgeHelper.getDifficultyColor(word.difficulty)

    // Pulsing animation for pronunciation state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val heartColor by animateColorAsState(
        targetValue = if (word.isSaved) CoralPink else LightText.copy(alpha = 0.85f),
        animationSpec = spring(),
        label = "heart_color"
    )

    val learnedColor by animateColorAsState(
        targetValue = if (word.isLearned) EmeraldGreen else LightText.copy(alpha = 0.85f),
        animationSpec = spring(),
        label = "learned_color"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DeepBackground,
                        categoryColor.copy(alpha = 0.12f),
                        DeepBackground,
                        categoryColor.copy(alpha = 0.08f)
                    )
                )
            )
    ) {
        // Ambient background glow circle
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(340.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            categoryColor.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 74.dp, top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Badges row (Category + Difficulty + Learned + Custom)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Category Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = categoryColor.copy(alpha = 0.18f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(categoryColor, categoryColor.copy(alpha = 0.4f)))),
                    modifier = Modifier.testTag("category_badge")
                ) {
                    Text(
                        text = word.category,
                        color = categoryColor,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Difficulty Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = difficultyColor.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(difficultyColor, difficultyColor.copy(alpha = 0.3f)))),
                    modifier = Modifier.testTag("difficulty_badge")
                ) {
                    Text(
                        text = word.difficulty,
                        color = difficultyColor,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Learned Status Badge
                if (word.isLearned) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = EmeraldGreen.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.6f)),
                        modifier = Modifier.testTag("learned_status_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Learned",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Learned",
                                color = EmeraldGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (word.isCustom) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = VibrantPurple.copy(alpha = 0.2f),
                        modifier = Modifier.testTag("custom_word_badge")
                    ) {
                        Text(
                            text = "Custom",
                            color = VibrantPurple,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Word text (large, prominent 36-44sp, bold)
            Text(
                text = word.word,
                style = MaterialTheme.typography.displayMedium,
                color = LightText,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vocab_word_title")
                    .semantics { role = Role.Button }
                    .clickable(onClick = onPronounce)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Phonetic pronunciation & Part of Speech
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (word.partOfSpeech.isNotBlank()) {
                    Text(
                        text = word.partOfSpeech.lowercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = categoryColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(categoryColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                if (word.phonetic.isNotBlank()) {
                    Text(
                        text = word.phonetic,
                        style = MaterialTheme.typography.titleMedium,
                        color = MutedText,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Definition & Progress Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DeepSurface.copy(alpha = 0.85f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(CardBorder.copy(alpha = 0.8f), categoryColor.copy(alpha = 0.3f))
                    )
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("word_meaning_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "DEFINITION",
                        style = MaterialTheme.typography.labelSmall,
                        color = categoryColor,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 25.sp),
                        color = LightText,
                        fontWeight = FontWeight.Medium
                    )

                    if (word.example.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepSurfaceVariant.copy(alpha = 0.7f))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "EXAMPLE CONTEXT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${word.example}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SubtitleText,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confidence Rating Selector (1 to 5 Stars)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepBackground.copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "YOUR CONFIDENCE",
                                style = MaterialTheme.typography.labelSmall,
                                color = AmberOrange,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )
                            val ratingLabel = when (word.confidenceRating) {
                                1 -> "Need practice"
                                2 -> "Learning"
                                3 -> "Familiar"
                                4 -> "Confident"
                                5 -> "Mastered"
                                else -> "Tap to rate"
                            }
                            Text(
                                text = ratingLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (word.confidenceRating > 0) AmberOrange else MutedText,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Star selector row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (star in 1..5) {
                                val isFilled = star <= word.confidenceRating
                                IconButton(
                                    onClick = { onSetConfidence(star) },
                                    modifier = Modifier
                                        .testTag("confidence_star_$star")
                                ) {
                                    Icon(
                                        imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Rate $star stars",
                                        tint = if (isFilled) AmberOrange else CardBorder,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Speech Speed segmented control (direct preset selection)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepBackground.copy(alpha = 0.5f))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SPEECH SPEED",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan,
                        letterSpacing = 1.sp,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(0.75f, 1.0f, 1.25f).forEach { rate ->
                        val isSelected = currentSpeechRate == rate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else CardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable(onClick = { onSetSpeechRate(rate) })
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (rate) {
                                    0.75f -> "0.75x"
                                    1.25f -> "1.25x"
                                    else -> "1.0x"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) NeonCyan else MutedText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation counter & review frequency info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Word $currentIndex of $totalCount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MutedText
                )
                if (word.reviewCount > 0) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelLarge,
                        color = MutedText.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "Reviewed ${word.reviewCount}x",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // TikTok-style Right Action Rail
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Heart / Save Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DeepSurface.copy(alpha = 0.9f))
                        .border(1.dp, if (word.isSaved) CoralPink.copy(alpha = 0.8f) else CardBorder, CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onToggleSave
                        )
                        .testTag("save_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (word.isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (word.isSaved) "Unsave word" else "Save word",
                        tint = heartColor,
                        modifier = Modifier
                            .size(26.dp)
                            .scale(if (word.isSaved) 1.1f else 1.0f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (word.isSaved) "Saved" else "Save",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (word.isSaved) CoralPink else MutedText,
                    fontSize = 11.sp
                )
            }

            // Learned Toggle Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (word.isLearned) EmeraldGreen.copy(alpha = 0.22f) else DeepSurface.copy(alpha = 0.9f))
                        .border(1.dp, if (word.isLearned) EmeraldGreen else CardBorder, CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onToggleLearned
                        )
                        .testTag("learned_toggle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (word.isLearned) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                        contentDescription = if (word.isLearned) "Mark as unlearned" else "Mark as learned",
                        tint = learnedColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (word.isLearned) "Learned" else "Learn",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (word.isLearned) EmeraldGreen else MutedText,
                    fontSize = 11.sp
                )
            }

            // Audio Pronunciation Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .scale(if (isSpeaking) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(if (isSpeaking) NeonCyan.copy(alpha = 0.25f) else DeepSurface.copy(alpha = 0.9f))
                        .border(
                            1.dp,
                            if (isSpeaking) NeonCyan else CardBorder,
                            CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onPronounce
                        )
                        .testTag("pronounce_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Filled.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Pronounce word",
                        tint = if (isSpeaking) NeonCyan else LightText,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSpeaking) "Speaking..." else "Listen",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSpeaking) NeonCyan else MutedText,
                    fontSize = 11.sp
                )
            }

            // Copy Word Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DeepSurface.copy(alpha = 0.85f))
                        .border(1.dp, CardBorder, CircleShape)
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Vocab Word", "${word.word}: ${word.meaning}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied \"${word.word}\" to clipboard", Toast.LENGTH_SHORT).show()
                        }
                        .testTag("copy_word_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy word and definition",
                        tint = SubtitleText,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Copy",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Quick Arrow Up (Previous) & Down (Next) navigation buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onPreviousWord,
                    modifier = Modifier
                        .size(40.dp)
                        .background(DeepSurfaceVariant.copy(alpha = 0.8f), CircleShape)
                        .testTag("previous_word_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous word",
                        tint = LightText,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onNextWord,
                    modifier = Modifier
                        .size(40.dp)
                        .background(DeepSurfaceVariant.copy(alpha = 0.8f), CircleShape)
                        .testTag("next_word_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next word",
                        tint = LightText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
