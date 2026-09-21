package com.example.vocab.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWordDialog(
    allWords: List<VocabWord>,
    onDismiss: () -> Unit,
    onSelectWord: (VocabWord) -> Unit,
    onPronounceWord: (String) -> Unit = {},
    onToggleSave: ((VocabWord) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedWordPreview by remember { mutableStateOf<VocabWord?>(null) }

    val filteredWords = remember(searchQuery, allWords) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            val query = searchQuery.trim().lowercase()
            allWords.asSequence()
                .filter {
                    it.word.lowercase().startsWith(query) ||
                            it.word.lowercase().contains(query) ||
                            it.meaning.lowercase().contains(query)
                }
                .sortedWith(
                    compareBy<VocabWord> {
                        // Prioritize exact or prefix match on word
                        if (it.word.equals(query, ignoreCase = true)) 0
                        else if (it.word.lowercase().startsWith(query)) 1
                        else 2
                    }.thenBy { it.word }
                )
                .take(30)
                .toList()
        }
    }

    // Auto-select best matching word for direct definition display
    val activeDefinitionWord by remember(searchQuery, selectedWordPreview, filteredWords) {
        derivedStateOf {
            selectedWordPreview ?: filteredWords.firstOrNull()
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DeepSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("search_word_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(ElectricIndigo, NeonCyan)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Dictionary & Word Lookup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LightText
                            )
                            Text(
                                text = "${allWords.size} words • Instant definition & sentence",
                                style = MaterialTheme.typography.bodySmall,
                                color = NeonCyan,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close search",
                            tint = MutedText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        selectedWordPreview = null
                    },
                    placeholder = {
                        Text(
                            text = "Search a word for meaning & sentence...",
                            color = MutedText,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                selectedWordPreview = null
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear input",
                                    tint = MutedText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DeepSurfaceVariant,
                        unfocusedContainerColor = DeepSurfaceVariant,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = LightText,
                        unfocusedTextColor = LightText
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dictionary_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Section header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "Search Results (${filteredWords.size})" else "Quick Suggestions",
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedText,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable container for Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Direct Meaning & Sentence Card (Shown when word is searched or selected)
                    if (activeDefinitionWord != null) {
                        val word = activeDefinitionWord!!
                        DirectWordDefinitionCard(
                            word = word,
                            onPronounce = { onPronounceWord(word.word) },
                            onGoToReel = {
                                onSelectWord(word)
                                onDismiss()
                            },
                            onToggleSave = { onToggleSave?.invoke(word) }
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Empty search state with quick discovery tags
                    if (searchQuery.isBlank() && selectedWordPreview == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Search any word to see its meaning & sentence",
                                color = LightText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Or tap a popular word below:",
                                color = MutedText,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val sampleWords = listOf("accordingly", "rizz", "paradigm", "aesthetic", "resilience", "algorithm")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                sampleWords.forEach { sample ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DeepSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                        modifier = Modifier.clickable {
                                            searchQuery = sample
                                            selectedWordPreview = allWords.firstOrNull { it.word.equals(sample, ignoreCase = true) }
                                        }
                                    ) {
                                        Text(
                                            text = sample,
                                            color = NeonCyan,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else if (filteredWords.isEmpty() && selectedWordPreview == null) {
                        // No exact match found
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = DeepSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = "No results",
                                    tint = AmberOrange,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No dictionary matches for \"$searchQuery\"",
                                    color = LightText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Check your spelling or try searching another word.",
                                    color = MutedText,
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // Other matching results list
                    if (filteredWords.size > 1) {
                        Text(
                            text = "Other Matches (${filteredWords.size}):",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            filteredWords.filter { it.id != activeDefinitionWord?.id }.take(15).forEach { match ->
                                SearchResultItem(
                                    word = match,
                                    onClick = {
                                        selectedWordPreview = match
                                    },
                                    onGoToFeed = {
                                        onSelectWord(match)
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Direct prominent card displaying the word, its meaning, and its example sentence
 */
@Composable
private fun DirectWordDefinitionCard(
    word: VocabWord,
    onPronounce: () -> Unit,
    onGoToReel: () -> Unit,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = CategoryBadgeHelper.getCategoryColor(word.category)
    val difficultyColor = CategoryBadgeHelper.getDifficultyColor(word.difficulty)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DeepSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row: Word title + phonetic + Audio button + Save button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LightText
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        if (word.phonetic.isNotBlank()) {
                            Text(
                                text = word.phonetic,
                                style = MaterialTheme.typography.bodySmall,
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (word.partOfSpeech.isNotBlank()) {
                            Text(
                                text = "• ${word.partOfSpeech}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedText,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Audio Pronunciation Button
                    Surface(
                        shape = CircleShape,
                        color = ElectricIndigo.copy(alpha = 0.25f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricIndigo.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = onPronounce)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Pronounce word",
                                tint = NeonCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Save toggle button
                    Surface(
                        shape = CircleShape,
                        color = if (word.isSaved) CoralPink.copy(alpha = 0.25f) else DeepSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (word.isSaved) CoralPink else CardBorder),
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = onToggleSave)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (word.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save word",
                                tint = if (word.isSaved) CoralPink else MutedText,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row (Category + Difficulty)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(categoryColor.copy(alpha = 0.15f))
                        .border(0.5.dp, categoryColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = word.category,
                        color = categoryColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(difficultyColor.copy(alpha = 0.15f))
                        .border(0.5.dp, difficultyColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = word.difficulty,
                        color = difficultyColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. MEANING SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepSurface.copy(alpha = 0.7f))
                    .padding(12.dp)
            ) {
                Text(
                    text = "MEANING",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. EXAMPLE SENTENCE SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepSurface.copy(alpha = 0.7f))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(ElectricIndigo.copy(alpha = 0.6f), Color.Transparent)),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "EXAMPLE SENTENCE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "“${word.example}”",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleText,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Actions: "Open in Full Reels Feed"
            Button(
                onClick = onGoToReel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricIndigo
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "View in Full Reel Feed",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    word: VocabWord,
    onClick: () -> Unit,
    onGoToFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = CategoryBadgeHelper.getCategoryColor(word.category)
    val difficultyColor = CategoryBadgeHelper.getDifficultyColor(word.difficulty)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DeepSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LightText
                    )
                    if (word.phonetic.isNotEmpty()) {
                        Text(
                            text = word.phonetic,
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan,
                            fontSize = 11.sp
                        )
                    }
                    if (word.partOfSpeech.isNotEmpty()) {
                        Text(
                            text = "• ${word.partOfSpeech}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = word.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = LightText.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Difficulty badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(difficultyColor.copy(alpha = 0.15f))
                        .border(0.5.dp, difficultyColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = word.difficulty,
                        color = difficultyColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onGoToFeed,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "View Reel",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
