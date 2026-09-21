package com.example.vocab.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocab.data.local.VocabDatabase
import com.example.vocab.data.model.DailyStats
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord
import com.example.vocab.data.repository.VocabRepository
import com.example.vocab.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class AppScreenTab {
    REEL,
    SAVED
}

enum class SavedTabFilter(val label: String) {
    ALL("All"),
    LIKED("Liked & Saved"),
    LEARNED("Learned")
}

data class CategoryProgressMetric(
    val category: String,
    val totalWords: Int,
    val learnedWords: Int,
    val likedWords: Int,
    val remainingWords: Int,
    val percentCompleted: Int
)

class VocabViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VocabRepository
    val ttsManager: TtsManager = TtsManager(application)

    private val _isInitialLoading = MutableStateFlow<Boolean>(true)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading.asStateFlow()

    private val _showMainMenu = MutableStateFlow<Boolean>(false)
    val showMainMenu: StateFlow<Boolean> = _showMainMenu.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>(VocabCategories.ALL)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow<String>("All Levels")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty.asStateFlow()

    private val _speechRate = MutableStateFlow<Float>(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _currentScreen = MutableStateFlow<AppScreenTab>(AppScreenTab.REEL)
    val currentScreen: StateFlow<AppScreenTab> = _currentScreen.asStateFlow()

    private val _savedSearchQuery = MutableStateFlow<String>("")
    val savedSearchQuery: StateFlow<String> = _savedSearchQuery.asStateFlow()

    private val _savedCategoryFilter = MutableStateFlow<String>(VocabCategories.ALL)
    val savedCategoryFilter: StateFlow<String> = _savedCategoryFilter.asStateFlow()

    private val _savedTabFilter = MutableStateFlow<SavedTabFilter>(SavedTabFilter.ALL)
    val savedTabFilter: StateFlow<SavedTabFilter> = _savedTabFilter.asStateFlow()

    private val _showGoalDialog = MutableStateFlow<Boolean>(false)
    val showGoalDialog: StateFlow<Boolean> = _showGoalDialog.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _jumpToWordId = MutableStateFlow<Long?>(null)
    val jumpToWordId: StateFlow<Long?> = _jumpToWordId.asStateFlow()

    // Shuffle seed for randomization engine
    private val _shuffleSeed = MutableStateFlow<Long>(System.currentTimeMillis())

    private fun getStartOfDayMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    init {
        val database = VocabDatabase.getDatabase(application)
        repository = VocabRepository(database.vocabDao(), application)
        viewModelScope.launch {
            repository.initializeDefaultWordsIfEmpty()
            _isInitialLoading.value = false
        }
        viewModelScope.launch {
            repository.allWords.collect { list ->
                if (list.isNotEmpty()) {
                    _isInitialLoading.value = false
                }
                latestWords = list
                val ids = list.map { it.id }
                if (ids != lastFeedWordIds) {
                    lastFeedWordIds = ids
                    buildOrderedFeed()
                }
            }
        }
    }

    val allWords: StateFlow<List<VocabWord>> = repository.allWords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedWords: StateFlow<List<VocabWord>> = repository.savedWords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val learnedWords: StateFlow<List<VocabWord>> = repository.learnedWords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedOrLearnedWords: StateFlow<List<VocabWord>> = repository.savedOrLearnedWords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val learnedWordsCount: StateFlow<Int> = repository.learnedWordsCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val reviewedTodayCount: StateFlow<Int> = repository.getReviewedTodayCount(getStartOfDayMillis())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val dailyGoal: StateFlow<Int> = repository.learningGoal
        .map { it?.dailyGoal ?: 10 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 10
        )

    // Daily Stats & Streak
    val todayDateStr: String = repository.getTodayDateString()
    val yesterdayDateStr: String = repository.getYesterdayDateString()

    val dailyStatsToday: StateFlow<DailyStats?> = repository.getDailyStats(todayDateStr)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyStatsYesterday: StateFlow<DailyStats?> = repository.getDailyStats(yesterdayDateStr)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allDailyStats: StateFlow<List<DailyStats>> = repository.allDailyStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviewsScrolledToday: StateFlow<Int> = dailyStatsToday
        .map { it?.reviewsScrolled ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val wordsLearnedToday: StateFlow<Int> = dailyStatsToday
        .map { it?.wordsLearned ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val wordsLearnedYesterday: StateFlow<Int> = dailyStatsYesterday
        .map { it?.wordsLearned ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val streakDays: StateFlow<Int> = combine(allDailyStats, dailyGoal, wordsLearnedToday) { stats, goal, learnedToday ->
        calculateStreak(stats, goal, learnedToday)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val bestStreakDays: StateFlow<Int> = combine(allDailyStats, dailyGoal) { stats, goal ->
        calculateBestStreak(stats, goal)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val overallConsistencyPercent: StateFlow<Int> = combine(allDailyStats, dailyGoal) { stats, goal ->
        if (stats.isEmpty()) {
            100
        } else {
            val achieved = stats.count { it.wordsLearned >= goal }
            ((achieved * 100) / stats.size).coerceIn(0, 100)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 100)

    // Category Progress Metrics for Main Menu Drawer
    val categoryProgressMetrics: StateFlow<List<CategoryProgressMetric>> = allWords
        .map { words ->
            VocabCategories.list.filter { it != VocabCategories.ALL }.map { cat ->
                val matching = words.filter { w ->
                    if (cat == VocabCategories.OXFORD_DICTIONARY) {
                        w.category == VocabCategories.OXFORD_DICTIONARY || w.category == VocabCategories.GENERAL_ENGLISH
                    } else {
                        w.category == cat
                    }
                }
                val total = matching.size
                val learned = matching.count { it.isLearned }
                val liked = matching.count { it.isSaved }
                val remaining = (total - learned).coerceAtLeast(0)
                val percent = if (total > 0) ((learned * 100) / total).coerceIn(0, 100) else 0
                CategoryProgressMetric(
                    category = cat,
                    totalWords = total,
                    learnedWords = learned,
                    likedWords = liked,
                    remainingWords = remaining,
                    percentCompleted = percent
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Confidence-driven feed. Order is a snapshot built on category/difficulty/seed
// changes or word-set membership changes, so rating a word in-session does NOT
// reorder the list under the swiping thumb. Ratings shape the next feed build.
private val _filteredWords = MutableStateFlow<List<VocabWord>>(emptyList())
val filteredWords: StateFlow<List<VocabWord>> = _filteredWords.asStateFlow()

private var latestWords: List<VocabWord> = emptyList()
private var lastFeedWordIds: List<Long>? = null

// ponytail: confidence sort is bucket-level, no SRS intervals. Add spaced
// repetition (due dates per rating) if review scheduling becomes a product goal.
private fun orderFeed(
    words: List<VocabWord>,
    category: String,
    difficulty: String,
    seed: Long
): List<VocabWord> {
    if (words.isEmpty()) return emptyList()

    val candidates = words.filter { word ->
        val matchesCategory = when (category) {
            VocabCategories.ALL -> true
            VocabCategories.OXFORD_DICTIONARY -> word.category == VocabCategories.OXFORD_DICTIONARY || word.category == VocabCategories.GENERAL_ENGLISH
            else -> word.category == category
        }
        val matchesDifficulty = (difficulty == "All Levels") || word.difficulty.equals(difficulty, ignoreCase = true)
        matchesCategory && matchesDifficulty
    }
    if (candidates.isEmpty()) return emptyList()

    val rng = java.util.Random(seed)
    // Lowest confidence first: unrated/weak words surface early each cycle,
    // "Mastered" words fall toward the back. Stable id tiebreak + seeded shuffle
    // within a bucket keep the order independent of live review-state writes.
    return if (category == VocabCategories.ALL) {
        val grouped = candidates.groupBy { it.category }
        val wordsByCat = grouped.mapValues { (_, list) ->
            list.sortedWith(compareBy<VocabWord> { it.confidenceRating }.thenBy { it.id })
                .shuffled(rng)
                .toMutableList()
        }

        val result = mutableListOf<VocabWord>()
        val cats = grouped.keys.shuffled(rng)
        var added = true
        while (added) {
            added = false
            for (cat in cats) {
                val list = wordsByCat[cat]
                if (list != null && list.isNotEmpty()) {
                    result.add(list.removeAt(0))
                    added = true
                }
            }
        }
        result
    } else {
        // Confidence buckets, seeded shuffle inside each, low rating first.
        candidates.groupBy { it.confidenceRating }
            .toSortedMap()
            .flatMap { (_, bucket) -> bucket.shuffled(rng) }
    }
}

private fun buildOrderedFeed() {
    if (latestWords.isEmpty()) return
    _filteredWords.value = orderFeed(latestWords, _selectedCategory.value, _selectedDifficulty.value, _shuffleSeed.value)
}

    val categoryCounts: StateFlow<Map<String, Int>> = combine(
        allWords,
        _selectedDifficulty
    ) { words: List<VocabWord>, diff: String ->
        val counts = mutableMapOf<String, Int>()
        val filteredByDiff = if (diff == "All Levels") words else words.filter { it.difficulty.equals(diff, ignoreCase = true) }
        counts[VocabCategories.ALL] = filteredByDiff.size
        VocabCategories.list.filter { it != VocabCategories.ALL }.forEach { cat: String ->
            counts[cat] = if (cat == VocabCategories.OXFORD_DICTIONARY) {
                filteredByDiff.count { it.category == VocabCategories.OXFORD_DICTIONARY || it.category == VocabCategories.GENERAL_ENGLISH }
            } else {
                filteredByDiff.count { it.category == cat }
            }
        }
        counts
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    val filteredSavedWords: StateFlow<List<VocabWord>> = combine(
        savedOrLearnedWords,
        _savedSearchQuery,
        _savedCategoryFilter,
        _savedTabFilter
    ) { list: List<VocabWord>, query: String, category: String, tabFilter: SavedTabFilter ->
        list.filter { word ->
            val matchesTab = when (tabFilter) {
                SavedTabFilter.ALL -> true
                SavedTabFilter.LIKED -> word.isSaved
                SavedTabFilter.LEARNED -> word.isLearned
            }
            val matchesQuery = query.isBlank() ||
                    word.word.contains(query, ignoreCase = true) ||
                    word.meaning.contains(query, ignoreCase = true) ||
                    word.example.contains(query, ignoreCase = true)
            val matchesCategory = when (category) {
                VocabCategories.ALL -> true
                VocabCategories.OXFORD_DICTIONARY -> word.category == VocabCategories.OXFORD_DICTIONARY || word.category == VocabCategories.GENERAL_ENGLISH
                else -> word.category == category
            }
            matchesTab && matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun reshuffleFeed() {
        _shuffleSeed.value = System.currentTimeMillis()
        buildOrderedFeed()
        _userMessage.value = "🎲 Feed reshuffled with fresh random order"
    }

    fun setMainMenuVisible(visible: Boolean) {
        _showMainMenu.value = visible
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        buildOrderedFeed()
    }

    fun selectDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
        buildOrderedFeed()
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        ttsManager.setSpeechRate(rate)
        val label = when (rate) {
            0.75f -> "Speed: 0.75x (Slow)"
            1.0f -> "Speed: 1.0x (Normal)"
            else -> "Speed: 1.25x (Fast)"
        }
        _userMessage.value = label
    }

    fun speakWord(word: String) {
        ttsManager.speak(word, _speechRate.value)
    }

    fun toggleSave(word: VocabWord) {
        viewModelScope.launch {
            val willBeSaved = !word.isSaved
            repository.toggleSave(word.id, word.isSaved)
            _userMessage.value = if (willBeSaved) "\"${word.word}\" saved to your collection" else "Removed from saved words"
        }
    }

    fun toggleLearned(word: VocabWord) {
        viewModelScope.launch {
            val willBeLearned = !word.isLearned
            repository.toggleLearned(word.id, word.isLearned)
            _userMessage.value = if (willBeLearned) {
                "✓ Marked \"${word.word}\" as Learned!"
            } else {
                "Marked \"${word.word}\" as Unlearned"
            }
        }
    }

    fun setConfidenceRating(word: VocabWord, rating: Int) {
        viewModelScope.launch {
            repository.setConfidenceRating(word.id, rating)
            val label = when (rating) {
                1 -> "Need practice (1★)"
                2 -> "Learning (2★)"
                3 -> "Familiar (3★)"
                4 -> "Confident (4★)"
                5 -> "Mastered (5★)"
                else -> "Rating updated"
            }
            _userMessage.value = "\"${word.word}\": $label"
        }
    }

    fun setDailyGoal(goal: Int) {
        viewModelScope.launch {
            repository.setDailyLearningGoal(goal)
            _showGoalDialog.value = false
            _userMessage.value = "🎯 Daily goal updated to $goal words/day"
        }
    }

    fun setGoalDialogVisible(visible: Boolean) {
        _showGoalDialog.value = visible
    }

    fun recordReview(wordId: Long) {
        viewModelScope.launch {
            repository.recordReview(wordId)
        }
    }

    fun deleteSavedWord(wordId: Long) {
        viewModelScope.launch {
            repository.toggleSave(wordId, true)
            _userMessage.value = "Word removed from saved collection"
        }
    }

    fun openSavedScreen(initialFilter: SavedTabFilter = SavedTabFilter.ALL) {
        _savedTabFilter.value = initialFilter
        _currentScreen.value = AppScreenTab.SAVED
    }

    fun setSavedTabFilter(filter: SavedTabFilter) {
        _savedTabFilter.value = filter
    }

    fun openReelScreen() {
        _currentScreen.value = AppScreenTab.REEL
    }

    fun jumpToWordInReels(word: VocabWord) {
        if (_selectedCategory.value != VocabCategories.ALL && _selectedCategory.value != word.category) {
            _selectedCategory.value = VocabCategories.ALL
        }
        if (_selectedDifficulty.value != "All Levels" && !_selectedDifficulty.value.equals(word.difficulty, ignoreCase = true)) {
            _selectedDifficulty.value = "All Levels"
        }
        _jumpToWordId.value = word.id
        _currentScreen.value = AppScreenTab.REEL
    }

    fun clearJumpTarget() {
        _jumpToWordId.value = null
    }

    fun setSavedSearchQuery(query: String) {
        _savedSearchQuery.value = query
    }

    fun setSavedCategoryFilter(category: String) {
        _savedCategoryFilter.value = category
    }

    fun bulkImportWords(jsonContent: String): Boolean {
        return try {
            val jsonArray = JSONArray(jsonContent)
            val newWords = mutableListOf<VocabWord>()
            for (i in 0 until jsonArray.length()) {
                val obj: JSONObject = jsonArray.getJSONObject(i)
                val word = obj.optString("word", "").trim()
                val meaning = obj.optString("meaning", "").trim()
                if (word.isNotEmpty() && meaning.isNotEmpty()) {
                    newWords.add(
                        VocabWord(
                            word = word,
                            phonetic = obj.optString("phonetic", "/${word.lowercase()}/"),
                            partOfSpeech = obj.optString("partOfSpeech", "noun"),
                            meaning = meaning,
                            example = obj.optString("example", "Used in context."),
                            category = obj.optString("category", VocabCategories.GENERAL_ENGLISH),
                            difficulty = obj.optString("difficulty", "Intermediate"),
                            isSaved = false,
                            isCustom = true
                        )
                    )
                }
            }
            if (newWords.isNotEmpty()) {
                viewModelScope.launch {
                    repository.bulkInsert(newWords)
                    _userMessage.value = "Successfully imported ${newWords.size} words!"
                }
                true
            } else {
                _userMessage.value = "No valid words found in JSON"
                false
            }
        } catch (e: Exception) {
            _userMessage.value = "Invalid JSON format: ${e.localizedMessage}"
            false
        }
    }

    fun exportSavedWordsJson(): String {
        val list = savedWords.value
        val array = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("word", item.word)
                put("phonetic", item.phonetic)
                put("partOfSpeech", item.partOfSpeech)
                put("meaning", item.meaning)
                put("example", item.example)
                put("category", item.category)
                put("difficulty", item.difficulty)
                put("isLearned", item.isLearned)
                put("confidenceRating", item.confidenceRating)
            }
            array.put(obj)
        }
        return array.toString(2)
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }

    private fun calculateStreak(stats: List<DailyStats>, goal: Int, learnedToday: Int): Int {
        val todayMet = learnedToday >= goal
        val statsMap = stats.associateBy { it.date }
        val yesterdayDate = repository.getYesterdayDateString()
        val yesterdayStat = statsMap[yesterdayDate]
        val yesterdayMet = yesterdayStat != null && yesterdayStat.wordsLearned >= goal

        if (!todayMet && !yesterdayMet) return 0

        var streak = if (todayMet) 1 else 0
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        while (true) {
            val dStr = sdf.format(cal.time)
            val s = statsMap[dStr]
            val met = s != null && s.wordsLearned >= goal
            if (met) {
                if (!todayMet && streak == 0) {
                    streak = 1
                } else {
                    streak++
                }
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateBestStreak(stats: List<DailyStats>, goal: Int): Int {
        if (stats.isEmpty()) return 0
        val sorted = stats.sortedBy { it.date }
        var best = 0
        var current = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var prevCal: Calendar? = null

        for (stat in sorted) {
            val met = stat.wordsLearned >= goal
            if (!met) {
                current = 0
                prevCal = null
                continue
            }
            val cal = Calendar.getInstance()
            try {
                cal.time = sdf.parse(stat.date) ?: continue
            } catch (e: Exception) {
                continue
            }
            if (prevCal == null) {
                current = 1
            } else {
                val diffDays = ((cal.timeInMillis - prevCal.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
                if (diffDays == 1) {
                    current++
                } else if (diffDays == 0) {
                    // Same date record
                } else {
                    current = 1
                }
            }
            prevCal = cal
            if (current > best) best = current
        }
        return best
    }
}
