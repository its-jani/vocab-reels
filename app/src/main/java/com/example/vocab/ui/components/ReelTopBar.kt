package com.example.vocab.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabCategories

@Composable
private fun CategoryDropdownItem(
    category: String,
    isSelected: Boolean,
    count: Int,
    onSelect: () -> Unit
) {
    val catColor = CategoryBadgeHelper.getCategoryColor(category)
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = category,
                    color = if (isSelected) catColor else LightText,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "$count words",
                    color = MutedText,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        onClick = onSelect,
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = catColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    )
}

@Composable
fun ReelTopBar(
    selectedCategory: String,
    selectedDifficulty: String,
    streakDays: Int,
    categoryCounts: Map<String, Int>,
    onSelectCategory: (String) -> Unit,
    onSelectDifficulty: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var difficultyMenuExpanded by remember { mutableStateOf(false) }

    val difficulties = listOf("All Levels", "Easy", "Intermediate", "Hard")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBackground)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Main Top Bar Row: Logo + App Title + Streak Counter <-> Search + Hamburger Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Brand Logo & Title & Streak Counter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vocab_logo),
                        contentDescription = "Vocab Reels Logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Text(
                        text = "Vocab Reels",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LightText
                    )

                    // Streak Counter Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AmberOrange.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberOrange.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable(onClick = onOpenMainMenu)
                            .testTag("streak_counter_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = AmberOrange,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${streakDays}d",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberOrange
                            )
                        }
                    }
                }

                // Actions: Search + 3-Line Hamburger Menu
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DeepSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .clickable(onClick = onOpenSearch)
                            .testTag("search_dictionary_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Search",
                                style = MaterialTheme.typography.labelMedium,
                                color = LightText
                            )
                        }
                    }

                    // Top Three-Line (Hamburger) Menu Button
                    IconButton(
                        onClick = onOpenMainMenu,
                        modifier = Modifier
                            .background(DeepSurfaceVariant, RoundedCornerShape(10.dp))
                            .border(1.dp, CardBorder.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                            .testTag("main_menu_hamburger_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Main Menu",
                            tint = LightText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Pills Row (Category & Difficulty Selectors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Filter Dropdown Trigger
                Box {
                    val categoryColor = CategoryBadgeHelper.getCategoryColor(selectedCategory)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DeepSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, categoryColor.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clickable { categoryMenuExpanded = true }
                            .testTag("category_dropdown_trigger")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Category filter",
                                tint = categoryColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedCategory,
                                style = MaterialTheme.typography.labelMedium,
                                color = LightText,
                                fontWeight = FontWeight.SemiBold
                            )
                            val count = categoryCounts[selectedCategory] ?: 0
                            if (count > 0) {
                                Text(
                                    text = " ($count)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = categoryColor
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Open category dropdown",
                                tint = MutedText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Category Dropdown Menu (grouped: All pinned, then 4 sections)
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false },
                        modifier = Modifier
                            .background(DeepSurfaceVariant)
                            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                    ) {
                        CategoryDropdownItem(
                            category = VocabCategories.ALL,
                            isSelected = selectedCategory == VocabCategories.ALL,
                            count = categoryCounts[VocabCategories.ALL] ?: 0,
                            onSelect = {
                                onSelectCategory(VocabCategories.ALL)
                                categoryMenuExpanded = false
                            }
                        )
                        VocabCategories.groupedList.forEach { (group, cats) ->
                            HorizontalDivider(color = CardBorder.copy(alpha = 0.5f))
                            Text(
                                text = group,
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedText,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                            cats.forEach { category ->
                                CategoryDropdownItem(
                                    category = category,
                                    isSelected = selectedCategory == category,
                                    count = categoryCounts[category] ?: 0,
                                    onSelect = {
                                        onSelectCategory(category)
                                        categoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Difficulty Filter Dropdown Trigger
                Box {
                    val diffColor = CategoryBadgeHelper.getDifficultyColor(selectedDifficulty)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DeepSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, diffColor.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { difficultyMenuExpanded = true }
                            .testTag("difficulty_dropdown_trigger")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Level: $selectedDifficulty",
                                style = MaterialTheme.typography.labelMedium,
                                color = diffColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Open difficulty dropdown",
                                tint = MutedText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Difficulty Dropdown Menu
                    DropdownMenu(
                        expanded = difficultyMenuExpanded,
                        onDismissRequest = { difficultyMenuExpanded = false },
                        modifier = Modifier
                            .background(DeepSurfaceVariant)
                            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                    ) {
                        difficulties.forEach { diff ->
                            val isSelected = diff == selectedDifficulty
                            val color = CategoryBadgeHelper.getDifficultyColor(diff)
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = diff,
                                        color = if (isSelected) color else LightText,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                onClick = {
                                    onSelectDifficulty(diff)
                                    difficultyMenuExpanded = false
                                },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
