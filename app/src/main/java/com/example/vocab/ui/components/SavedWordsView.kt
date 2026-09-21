package com.example.vocab.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord
import com.example.vocab.ui.SavedTabFilter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SavedWordsView(
    savedWords: List<VocabWord>,
    totalSavedCount: Int,
    totalLearnedCount: Int,
    totalCollectionCount: Int,
    activeTabFilter: SavedTabFilter,
    onTabFilterChange: (SavedTabFilter) -> Unit,
    searchQuery: String,
    selectedCategory: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onBackToReels: () -> Unit,
    onToggleSave: (VocabWord) -> Unit,
    onToggleLearned: (VocabWord) -> Unit,
    onSetConfidence: (VocabWord, Int) -> Unit,
    onPronounceWord: (String) -> Unit,
    onJumpToWord: (VocabWord) -> Unit,
    onExportJson: () -> String,
    onImportJson: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }
    var showExportDialog by remember { mutableStateOf(false) }
    var exportedJsonText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onBackToReels,
                    modifier = Modifier
                        .size(40.dp)
                        .background(DeepSurfaceVariant, CircleShape)
                        .testTag("saved_words_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to reels feed",
                        tint = LightText
                    )
                }
                Column {
                    Text(
                        text = when (activeTabFilter) {
                            SavedTabFilter.LIKED -> "Liked Words"
                            SavedTabFilter.LEARNED -> "Learned Words"
                            SavedTabFilter.ALL -> "My Word Collection"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LightText
                    )
                    Text(
                        text = "${savedWords.size} words showing",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )
                }
            }

            // Export / Backup action
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = {
                        exportedJsonText = onExportJson()
                        showExportDialog = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DeepSurfaceVariant, CircleShape)
                        .testTag("export_saved_words_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Export saved words as JSON",
                        tint = SubtitleText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {
                        importJsonText = ""
                        showImportDialog = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DeepSurfaceVariant, CircleShape)
                        .testTag("import_words_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = "Import words via JSON",
                        tint = SubtitleText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Segmented Tab Row for Liked, Learned, All
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DeepSurfaceVariant)
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Liked & Saved Tab
            val isLikedSelected = activeTabFilter == SavedTabFilter.LIKED
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (isLikedSelected) CoralPink.copy(alpha = 0.25f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isLikedSelected) CoralPink else Color.Transparent,
                        RoundedCornerShape(11.dp)
                    )
                    .clickable { onTabFilterChange(SavedTabFilter.LIKED) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (isLikedSelected) CoralPink else MutedText,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Liked ($totalSavedCount)",
                        color = if (isLikedSelected) LightText else MutedText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isLikedSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            // Learned Tab
            val isLearnedSelected = activeTabFilter == SavedTabFilter.LEARNED
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (isLearnedSelected) EmeraldGreen.copy(alpha = 0.25f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isLearnedSelected) EmeraldGreen else Color.Transparent,
                        RoundedCornerShape(11.dp)
                    )
                    .clickable { onTabFilterChange(SavedTabFilter.LEARNED) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isLearnedSelected) EmeraldGreen else MutedText,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Learned ($totalLearnedCount)",
                        color = if (isLearnedSelected) LightText else MutedText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isLearnedSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            // All Collection Tab
            val isAllSelected = activeTabFilter == SavedTabFilter.ALL
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (isAllSelected) ElectricIndigo.copy(alpha = 0.35f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isAllSelected) ElectricIndigo else Color.Transparent,
                        RoundedCornerShape(11.dp)
                    )
                    .clickable { onTabFilterChange(SavedTabFilter.ALL) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All ($totalCollectionCount)",
                    color = if (isAllSelected) LightText else MutedText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }

        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text("Search word, definition, or context...", color = MutedText)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonCyan
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MutedText
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DeepSurface,
                unfocusedContainerColor = DeepSurface,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = LightText,
                unfocusedTextColor = LightText
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("saved_words_search_field")
        )

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VocabCategories.list.forEach { category ->
                val isSelected = category == selectedCategory
                val categoryColor = CategoryBadgeHelper.getCategoryColor(category)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) categoryColor.copy(alpha = 0.22f) else DeepSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) categoryColor else CardBorder
                    ),
                    modifier = Modifier
                        .clickable { onSelectCategory(category) }
                        .testTag("saved_cat_chip_$category")
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) categoryColor else SubtitleText,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Saved words list or empty state
        if (savedWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(DeepSurfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "No saved words",
                            tint = CoralPink,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "No matching words found"
                        } else when (activeTabFilter) {
                            SavedTabFilter.LIKED -> "No liked words yet"
                            SavedTabFilter.LEARNED -> "No learned words yet"
                            SavedTabFilter.ALL -> "Your collection is empty"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = LightText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "Try a different search query or clear the filter."
                        } else when (activeTabFilter) {
                            SavedTabFilter.LIKED -> "Tap the ❤️ heart icon on any card in the reels feed to add words to your liked list."
                            SavedTabFilter.LEARNED -> "Tap the ✓ Learn button on any card in the reels feed to mark words as learned."
                            SavedTabFilter.ALL -> "Tap ❤️ Like or ✓ Learn on any word in the reels feed to grow your vocabulary collection."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onBackToReels,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Explore Words in Reels")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedWords, key = { it.id }) { word ->
                    SavedWordCard(
                        word = word,
                        onPronounce = { onPronounceWord(word.word) },
                        onToggleSave = { onToggleSave(word) },
                        onToggleLearned = { onToggleLearned(word) },
                        onSetConfidence = { rating -> onSetConfidence(word, rating) },
                        onPracticeInReels = { onJumpToWord(word) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Saved Vocabulary", color = LightText) },
            text = {
                Column {
                    Text(
                        text = "Your saved words in JSON format (${savedWords.size} words):",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubtitleText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepSurfaceVariant)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = exportedJsonText,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = LightText
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Saved Vocabulary JSON", exportedJsonText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied JSON to clipboard", Toast.LENGTH_SHORT).show()
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                ) {
                    Text("Copy JSON")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = MutedText)
                }
            },
            containerColor = DeepSurface
        )
    }

    // Import Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Vocabulary (JSON)", color = LightText) },
            text = {
                Column {
                    Text(
                        text = "Paste JSON array containing words with word, meaning, category, and difficulty:",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubtitleText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = { importJsonText = it },
                        placeholder = { Text("[{\"word\": \"Example\", \"meaning\": \"...\"}]", color = MutedText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LightText,
                            unfocusedTextColor = LightText,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (importJsonText.isNotBlank()) {
                            val success = onImportJson(importJsonText)
                            if (success) {
                                showImportDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel", color = MutedText)
                }
            },
            containerColor = DeepSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SavedWordCard(
    word: VocabWord,
    onPronounce: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleLearned: () -> Unit,
    onSetConfidence: (Int) -> Unit,
    onPracticeInReels: () -> Unit
) {
    val categoryColor = CategoryBadgeHelper.getCategoryColor(word.category)
    val diffColor = CategoryBadgeHelper.getDifficultyColor(word.difficulty)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                listOf(CardBorder, categoryColor.copy(alpha = 0.3f))
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_word_card_${word.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Word + Badges + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = word.word,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LightText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (word.isLearned) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = EmeraldGreen.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Learned",
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Learned",
                                        color = EmeraldGreen,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (word.partOfSpeech.isNotBlank()) {
                            Text(
                                text = word.partOfSpeech.lowercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = categoryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (word.phonetic.isNotBlank()) {
                            Text(
                                text = word.phonetic,
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedText,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }

                // Action buttons: Like/Saved, Learned, Pronounce, Practice
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Like / Saved Toggle
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (word.isSaved) CoralPink.copy(alpha = 0.2f) else DeepSurfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (word.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (word.isSaved) "Remove from liked" else "Add to liked",
                            tint = if (word.isSaved) CoralPink else SubtitleText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Learned Toggle
                    IconButton(
                        onClick = onToggleLearned,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (word.isLearned) EmeraldGreen.copy(alpha = 0.2f) else DeepSurfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (word.isLearned) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                            contentDescription = if (word.isLearned) "Mark as unlearned" else "Mark as learned",
                            tint = if (word.isLearned) EmeraldGreen else SubtitleText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Pronounce Audio
                    IconButton(
                        onClick = onPronounce,
                        modifier = Modifier
                            .size(48.dp)
                            .background(DeepSurfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce word",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Meaning
            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium,
                color = SubtitleText
            )

            if (word.example.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "\"${word.example}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Star Rating row (reel card labels this control "YOUR CONFIDENCE")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (star in 1..5) {
                    val isFilled = star <= word.confidenceRating
                    IconButton(
                        onClick = { onSetConfidence(star) },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Rate $star stars",
                            tint = if (isFilled) AmberOrange else CardBorder,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer row: Category/Difficulty chips & "Practice in Reels" button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = categoryColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = word.category,
                            color = categoryColor,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = diffColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = word.difficulty,
                            color = diffColor,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Practice in Reels button
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DeepSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.clickable(onClick = onPracticeInReels)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Practice in Reels",
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "View in Reel",
                            style = MaterialTheme.typography.labelSmall,
                            color = LightText
                        )
                    }
                }
            }
        }
    }
}
