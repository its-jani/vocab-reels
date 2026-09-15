package com.example.vocab.data.repository

import android.content.Context
import android.util.JsonReader
import com.example.vocab.data.local.DefaultVocabulary
import com.example.vocab.data.local.VocabDao
import com.example.vocab.data.model.UserLearningGoal
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class VocabRepository(
    private val vocabDao: VocabDao,
    private val context: Context
) {

    val allWords: Flow<List<VocabWord>> = vocabDao.getAllWords()
    val savedWords: Flow<List<VocabWord>> = vocabDao.getSavedWords()
    val learnedWords: Flow<List<VocabWord>> = vocabDao.getLearnedWords()
    val savedOrLearnedWords: Flow<List<VocabWord>> = vocabDao.getSavedOrLearnedWords()
    val learnedWordsCount: Flow<Int> = vocabDao.getLearnedWordsCount()
    val learningGoal: Flow<UserLearningGoal?> = vocabDao.getLearningGoal()
    val allDailyStats: Flow<List<com.example.vocab.data.model.DailyStats>> = vocabDao.getAllDailyStats()

    fun getTodayDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun getYesterdayDateString(): String {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(cal.time)
    }

    fun getDailyStats(date: String): Flow<com.example.vocab.data.model.DailyStats?> {
        return vocabDao.getDailyStats(date)
    }

    fun getReviewedTodayCount(startOfDay: Long): Flow<Int> {
        return vocabDao.getReviewedTodayCount(startOfDay)
    }

    suspend fun initializeDefaultWordsIfEmpty() {
        withContext(Dispatchers.IO) {
            val existingWords = vocabDao.getAllWordsSnapshot()
            val existingMap = existingWords.associateBy { it.word.lowercase().trim() }
            val wordsToInsert = mutableListOf<VocabWord>()
            val wordsToUpdate = mutableListOf<VocabWord>()

            // Purge basic/overly common words if present
            val basicWordsToRemove = setOf(
                "stone", "pipe", "accept", "reject", "access", "accessible", "accidentally",
                "action", "actor", "activity", "apple", "table", "chair", "door", "window",
                "shoe", "hat", "cup", "pen", "box", "ball", "milk", "water", "bread", "bed",
                "egg", "fish", "cat", "dog", "boy", "girl", "baby"
            )
            for (bw in basicWordsToRemove) {
                val match = existingMap[bw]
                if (match != null && !match.isCustom) {
                    vocabDao.deleteWordById(match.id)
                }
            }

            // 1. Curated domain words if not already present
            for (sample in DefaultVocabulary.sampleWords) {
                val key = sample.word.lowercase().trim()
                val existing = existingMap[key]
                if (existing == null) {
                    wordsToInsert.add(sample)
                } else if (!existing.isCustom && (existing.category != sample.category || existing.difficulty != sample.difficulty)) {
                    wordsToUpdate.add(
                        existing.copy(
                            category = sample.category,
                            difficulty = sample.difficulty,
                            meaning = sample.meaning,
                            example = sample.example
                        )
                    )
                }
            }

            // 2. Stream load words from oxford_words.json
            try {
                context.assets.open("oxford_words.json").use { inputStream ->
                    JsonReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                        reader.beginArray()
                        while (reader.hasNext()) {
                            var word = ""
                            var phonetic = ""
                            var partOfSpeech = ""
                            var meaning = ""
                            var example = ""
                            var category = VocabCategories.GENERAL_ENGLISH
                            var difficulty = "Easy"

                            reader.beginObject()
                            while (reader.hasNext()) {
                                when (reader.nextName()) {
                                    "w" -> word = reader.nextString()
                                    "p" -> phonetic = reader.nextString()
                                    "t" -> partOfSpeech = reader.nextString()
                                    "m" -> meaning = reader.nextString()
                                    "e" -> example = reader.nextString()
                                    "c" -> category = reader.nextString()
                                    "d" -> difficulty = reader.nextString()
                                    else -> reader.skipValue()
                                }
                            }
                            reader.endObject()

                            val normalizedWord = word.trim()
                            if (normalizedWord.isNotEmpty() && meaning.isNotBlank()) {
                                val key = normalizedWord.lowercase()
                                val existing = existingMap[key]
                                if (existing == null) {
                                    wordsToInsert.add(
                                        VocabWord(
                                            word = normalizedWord,
                                            phonetic = phonetic.trim(),
                                            partOfSpeech = partOfSpeech.trim(),
                                            meaning = meaning.trim(),
                                            example = example.trim(),
                                            category = category,
                                            difficulty = difficulty
                                        )
                                    )
                                } else if (!existing.isCustom && (existing.category != category || existing.difficulty != difficulty)) {
                                    wordsToUpdate.add(
                                        existing.copy(
                                            category = category,
                                            difficulty = difficulty,
                                            phonetic = if (existing.phonetic.isBlank()) phonetic.trim() else existing.phonetic,
                                            partOfSpeech = if (existing.partOfSpeech.isBlank()) partOfSpeech.trim() else existing.partOfSpeech
                                        )
                                    )
                                }
                            }
                        }
                        reader.endArray()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (wordsToInsert.isNotEmpty()) {
                wordsToInsert.chunked(300).forEach { batch ->
                    vocabDao.insertWords(batch)
                }
            }

            if (wordsToUpdate.isNotEmpty()) {
                wordsToUpdate.chunked(300).forEach { batch ->
                    vocabDao.insertWords(batch)
                }
            }
        }
    }

    suspend fun toggleSave(wordId: Long, isCurrentlySaved: Boolean) {
        withContext(Dispatchers.IO) {
            val newStatus = !isCurrentlySaved
            val timestamp = if (newStatus) System.currentTimeMillis() else 0L
            vocabDao.updateSavedStatus(wordId, newStatus, timestamp)
        }
    }

    suspend fun toggleLearned(wordId: Long, isCurrentlyLearned: Boolean) {
        withContext(Dispatchers.IO) {
            val newStatus = !isCurrentlyLearned
            val timestamp = System.currentTimeMillis()
            vocabDao.updateLearnedStatus(wordId, newStatus, timestamp)
            val today = getTodayDateString()
            val current = vocabDao.getDailyStatsSnapshot(today)
            val delta = if (newStatus) 1 else -1
            val newLearnedCount = ((current?.wordsLearned ?: 0) + delta).coerceAtLeast(0)
            val updated = current?.copy(
                wordsLearned = newLearnedCount,
                updatedAt = timestamp
            ) ?: com.example.vocab.data.model.DailyStats(date = today, reviewsScrolled = 0, wordsLearned = if (newStatus) 1 else 0, updatedAt = timestamp)
            vocabDao.insertOrUpdateDailyStats(updated)
        }
    }

    suspend fun setConfidenceRating(wordId: Long, rating: Int) {
        withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            vocabDao.updateConfidenceRating(wordId, rating, timestamp)
        }
    }

    suspend fun recordReview(wordId: Long) {
        withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            vocabDao.recordWordReview(wordId, timestamp)
            val today = getTodayDateString()
            val current = vocabDao.getDailyStatsSnapshot(today)
            val updated = current?.copy(
                reviewsScrolled = current.reviewsScrolled + 1,
                updatedAt = timestamp
            ) ?: com.example.vocab.data.model.DailyStats(date = today, reviewsScrolled = 1, wordsLearned = 0, updatedAt = timestamp)
            vocabDao.insertOrUpdateDailyStats(updated)
        }
    }

    suspend fun setDailyLearningGoal(goalCount: Int) {
        withContext(Dispatchers.IO) {
            vocabDao.setLearningGoal(UserLearningGoal(id = 1, dailyGoal = goalCount))
        }
    }

    suspend fun addWord(
        word: String,
        phonetic: String,
        partOfSpeech: String,
        meaning: String,
        example: String,
        category: String,
        difficulty: String
    ): Long {
        return withContext(Dispatchers.IO) {
            val item = VocabWord(
                word = word.trim(),
                phonetic = phonetic.trim(),
                partOfSpeech = partOfSpeech.trim(),
                meaning = meaning.trim(),
                example = example.trim(),
                category = category,
                difficulty = difficulty,
                isSaved = true,
                savedAt = System.currentTimeMillis(),
                isCustom = true
            )
            vocabDao.insertWord(item)
        }
    }

    suspend fun deleteWord(wordId: Long) {
        withContext(Dispatchers.IO) {
            vocabDao.deleteWordById(wordId)
        }
    }

    suspend fun bulkInsert(words: List<VocabWord>) {
        withContext(Dispatchers.IO) {
            words.chunked(300).forEach { batch ->
                vocabDao.insertWords(batch)
            }
        }
    }

    suspend fun resetDefaults() {
        withContext(Dispatchers.IO) {
            vocabDao.clearDefaultWords()
        }
        initializeDefaultWordsIfEmpty()
    }
}
