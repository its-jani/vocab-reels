package com.example.vocab.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord
import com.example.vocab.ui.components.DailyGoalDialog
import com.example.vocab.ui.components.MainMenuBottomSheet
import com.example.vocab.ui.components.ReelCard
import com.example.vocab.ui.components.ReelTopBar
import com.example.vocab.ui.components.SavedWordsView
import com.example.vocab.ui.components.SearchWordDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabReelsApp(
    viewModel: VocabViewModel,
    modifier: Modifier = Modifier
) {
    val isInitialLoading by viewModel.isInitialLoading.collectAsStateWithLifecycle()
    val allWords by viewModel.allWords.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val filteredWords by viewModel.filteredWords.collectAsStateWithLifecycle()
    val savedWords by viewModel.savedWords.collectAsStateWithLifecycle()
    val learnedWords by viewModel.learnedWords.collectAsStateWithLifecycle()
    val savedOrLearnedWords by viewModel.savedOrLearnedWords.collectAsStateWithLifecycle()
    val savedTabFilter by viewModel.savedTabFilter.collectAsStateWithLifecycle()
    val filteredSavedWords by viewModel.filteredSavedWords.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
    val categoryCounts by viewModel.categoryCounts.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsStateWithLifecycle()
    val speakingWord by viewModel.ttsManager.currentlySpeakingWord.collectAsStateWithLifecycle()
    val speechRate by viewModel.speechRate.collectAsStateWithLifecycle()
    val showGoalDialog by viewModel.showGoalDialog.collectAsStateWithLifecycle()
    val showMainMenu by viewModel.showMainMenu.collectAsStateWithLifecycle()
    var showSearchDialog by remember { mutableStateOf(false) }
    val dailyGoal by viewModel.dailyGoal.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val jumpToWordId by viewModel.jumpToWordId.collectAsStateWithLifecycle()
    val savedSearchQuery by viewModel.savedSearchQuery.collectAsStateWithLifecycle()
    val savedCategoryFilter by viewModel.savedCategoryFilter.collectAsStateWithLifecycle()

    // Analytics & Metrics for Main Menu
    val streakDays by viewModel.streakDays.collectAsStateWithLifecycle()
    val bestStreakDays by viewModel.bestStreakDays.collectAsStateWithLifecycle()
    val overallConsistencyPercent by viewModel.overallConsistencyPercent.collectAsStateWithLifecycle()
    val reviewsScrolledToday by viewModel.reviewsScrolledToday.collectAsStateWithLifecycle()
    val wordsLearnedToday by viewModel.wordsLearnedToday.collectAsStateWithLifecycle()
    val wordsLearnedYesterday by viewModel.wordsLearnedYesterday.collectAsStateWithLifecycle()
    val categoryProgressMetrics by viewModel.categoryProgressMetrics.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Show feedback messages
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DeepBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (isInitialLoading) {
            // Branded Loading State: Prevents "No words found" flashing on startup
            StartupLoadingView()
        } else {
            Crossfade(
                targetState = currentScreen,
                label = "screen_crossfade",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { screen ->
                when (screen) {
                    AppScreenTab.REEL -> {
                        ReelsFeedScreen(
                            words = filteredWords,
                            allWords = allWords,
                            selectedCategory = selectedCategory,
                            selectedDifficulty = selectedDifficulty,
                            categoryCounts = categoryCounts,
                            streakDays = streakDays,
                            isSpeaking = isSpeaking,
                            speakingWord = speakingWord,
                            speechRate = speechRate,
                            jumpToWordId = jumpToWordId,
                            onSelectCategory = { viewModel.selectCategory(it) },
                            onSelectDifficulty = { viewModel.selectDifficulty(it) },
                            onOpenSearch = { showSearchDialog = true },
                            onOpenMainMenu = { viewModel.setMainMenuVisible(true) },
                            onPronounceWord = { viewModel.speakWord(it) },
                            onToggleSave = { viewModel.toggleSave(it) },
                            onToggleLearned = { viewModel.toggleLearned(it) },
                            onSetConfidence = { word, rating -> viewModel.setConfidenceRating(word, rating) },
                            onRecordReview = { wordId -> viewModel.recordReview(wordId) },
                            onSetSpeechRate = { rate -> viewModel.setSpeechRate(rate) },
                            onClearJumpTarget = { viewModel.clearJumpTarget() },
                            onResetFilters = {
                                viewModel.selectCategory(VocabCategories.ALL)
                                viewModel.selectDifficulty("All Levels")
                            }
                        )
                    }
                    AppScreenTab.SAVED -> {
                        SavedWordsView(
                            savedWords = filteredSavedWords,
                            totalSavedCount = savedWords.size,
                            totalLearnedCount = learnedWords.size,
                            totalCollectionCount = savedOrLearnedWords.size,
                            activeTabFilter = savedTabFilter,
                            onTabFilterChange = { viewModel.setSavedTabFilter(it) },
                            searchQuery = savedSearchQuery,
                            selectedCategory = savedCategoryFilter,
                            onSearchQueryChange = { viewModel.setSavedSearchQuery(it) },
                            onSelectCategory = { viewModel.setSavedCategoryFilter(it) },
                            onBackToReels = { viewModel.openReelScreen() },
                            onToggleSave = { viewModel.toggleSave(it) },
                            onToggleLearned = { viewModel.toggleLearned(it) },
                            onSetConfidence = { word, rating -> viewModel.setConfidenceRating(word, rating) },
                            onPronounceWord = { viewModel.speakWord(it) },
                            onJumpToWord = { viewModel.jumpToWordInReels(it) },
                            onExportJson = { viewModel.exportSavedWordsJson() },
                            onImportJson = { viewModel.bulkImportWords(it) }
                        )
                    }
                }
            }
        }

        // Top 3-line Hamburger Main Menu Bottom Sheet
        if (showMainMenu) {
            MainMenuBottomSheet(
                onDismiss = { viewModel.setMainMenuVisible(false) },
                reviewsScrolledToday = reviewsScrolledToday,
                wordsLearnedToday = wordsLearnedToday,
                wordsLearnedYesterday = wordsLearnedYesterday,
                streakDays = streakDays,
                bestStreakDays = bestStreakDays,
                consistencyPercent = overallConsistencyPercent,
                dailyGoal = dailyGoal,
                categoryMetrics = categoryProgressMetrics,
                onSelectCategory = { cat ->
                    viewModel.selectCategory(cat)
                    viewModel.setMainMenuVisible(false)
                },
                onOpenLikedWords = {
                    viewModel.openSavedScreen(SavedTabFilter.LIKED)
                    viewModel.setMainMenuVisible(false)
                },
                onOpenLearnedWords = {
                    viewModel.openSavedScreen(SavedTabFilter.LEARNED)
                    viewModel.setMainMenuVisible(false)
                },
                onOpenGoalDialog = {
                    viewModel.setGoalDialogVisible(true)
                    viewModel.setMainMenuVisible(false)
                },
                onReshuffleFeed = {
                    viewModel.reshuffleFeed()
                    viewModel.setMainMenuVisible(false)
                }
            )
        }

        // Quick Search Oxford Dictionary Dialog
        if (showSearchDialog) {
            SearchWordDialog(
                allWords = allWords,
                onDismiss = { showSearchDialog = false },
                onSelectWord = { word ->
                    viewModel.jumpToWordInReels(word)
                },
                onPronounceWord = { wordStr ->
                    viewModel.speakWord(wordStr)
                },
                onToggleSave = { word ->
                    viewModel.toggleSave(word)
                }
            )
        }

        // Set daily goal dialog
        if (showGoalDialog) {
            DailyGoalDialog(
                currentGoal = dailyGoal,
                learnedToday = wordsLearnedToday,
                onDismiss = { viewModel.setGoalDialogVisible(false) },
                onSaveGoal = { newGoal -> viewModel.setDailyGoal(newGoal) }
            )
        }
    }
}

@Composable
private fun StartupLoadingView() {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alpha by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_vocab_logo),
                contentDescription = "Vocab Reels Logo",
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .alpha(alpha)
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                color = ElectricIndigo,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Curating your vocabulary feed...",
                style = MaterialTheme.typography.titleMedium,
                color = LightText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Personalizing high-yield words, corporate terms & idioms",
                style = MaterialTheme.typography.bodySmall,
                color = MutedText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ReelsFeedScreen(
    words: List<VocabWord>,
    allWords: List<VocabWord>,
    selectedCategory: String,
    selectedDifficulty: String,
    categoryCounts: Map<String, Int>,
    streakDays: Int,
    isSpeaking: Boolean,
    speakingWord: String?,
    speechRate: Float,
    jumpToWordId: Long?,
    onSelectCategory: (String) -> Unit,
    onSelectDifficulty: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenMainMenu: () -> Unit,
    onPronounceWord: (String) -> Unit,
    onToggleSave: (VocabWord) -> Unit,
    onToggleLearned: (VocabWord) -> Unit,
    onSetConfidence: (VocabWord, Int) -> Unit,
    onRecordReview: (Long) -> Unit,
    onSetSpeechRate: (Float) -> Unit,
    onClearJumpTarget: () -> Unit,
    onResetFilters: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Large infinite loop buffer (100,000 items) centered so scrolling works both up and down infinitely
    val loopCount = if (words.isNotEmpty()) 100_000 else 0
    val middlePage = if (words.isNotEmpty()) (loopCount / 2) - ((loopCount / 2) % words.size) else 0

    val pagerState = rememberPagerState(
        initialPage = middlePage,
        pageCount = { loopCount }
    )

    // Handle jumping to a specific word (from Saved Words list or Search)
    LaunchedEffect(jumpToWordId, words) {
        if (jumpToWordId != null && words.isNotEmpty()) {
            val targetIndex = words.indexOfFirst { it.id == jumpToWordId }
            if (targetIndex >= 0) {
                val currentModulo = pagerState.currentPage % words.size
                val diff = targetIndex - currentModulo
                val targetPage = pagerState.currentPage + diff
                pagerState.scrollToPage(targetPage)
            }
            onClearJumpTarget()
        }
    }

    // Record a review only when the visible word changes, not when its database row updates.
    val currentWordId = if (words.isNotEmpty()) {
        words[pagerState.currentPage % words.size].id
    } else {
        null
    }
    var hasDisplayedInitialWord by remember { mutableStateOf(false) }
    LaunchedEffect(currentWordId) {
        currentWordId?.let { wordId ->
            if (hasDisplayedInitialWord) {
                onRecordReview(wordId)
            } else {
                hasDisplayedInitialWord = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground)
            .statusBarsPadding()
    ) {
        // Top Bar at the very top (no double insets, no wasted gap, clean layout)
        ReelTopBar(
            selectedCategory = selectedCategory,
            selectedDifficulty = selectedDifficulty,
            streakDays = streakDays,
            categoryCounts = categoryCounts,
            onSelectCategory = onSelectCategory,
            onSelectDifficulty = onSelectDifficulty,
            onOpenSearch = onOpenSearch,
            onOpenMainMenu = onOpenMainMenu,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (words.isEmpty()) {
                // Empty state if filters match 0 words
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(DeepSurfaceVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = "No words found",
                                tint = NeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No words for $selectedCategory ($selectedDifficulty)",
                            style = MaterialTheme.typography.titleMedium,
                            color = LightText,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try clearing your filters or selecting a different category.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onResetFilters,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                // Infinite Vertical Pager (TikTok/Reels style) with smooth snapping fling behavior
                VerticalPager(
                    state = pagerState,
                    beyondViewportPageCount = 1,
                    flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("infinite_reels_vertical_pager")
                ) { page ->
                    val wordIndex = page % words.size
                    val currentWord = words[wordIndex]
                    val isCurrentWordSpeaking = isSpeaking && speakingWord == currentWord.word

                    ReelCard(
                        word = currentWord,
                        currentIndex = wordIndex + 1,
                        totalCount = words.size,
                        isSpeaking = isCurrentWordSpeaking,
                        currentSpeechRate = speechRate,
                        onPronounce = { onPronounceWord(currentWord.word) },
                        onToggleSave = { onToggleSave(currentWord) },
                        onToggleLearned = { onToggleLearned(currentWord) },
                        onSetConfidence = { rating -> onSetConfidence(currentWord, rating) },
                        onSetSpeechRate = onSetSpeechRate,
                        onPreviousWord = {
                            coroutineScope.launch {
                                if (pagerState.currentPage > 0) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        },
                        onNextWord = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    )
                }
            }
        }
    }
}

