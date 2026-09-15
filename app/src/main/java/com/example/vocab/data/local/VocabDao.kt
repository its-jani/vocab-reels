package com.example.vocab.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vocab.data.model.DailyStats
import com.example.vocab.data.model.UserLearningGoal
import com.example.vocab.data.model.VocabWord
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab_words ORDER BY id ASC")
    fun getAllWords(): Flow<List<VocabWord>>

    @Query("SELECT * FROM vocab_words WHERE isSaved = 1 ORDER BY savedAt DESC")
    fun getSavedWords(): Flow<List<VocabWord>>

    @Query("SELECT * FROM vocab_words WHERE isLearned = 1 ORDER BY lastReviewedAt DESC")
    fun getLearnedWords(): Flow<List<VocabWord>>

    @Query("SELECT * FROM vocab_words WHERE isSaved = 1 OR isLearned = 1 ORDER BY CASE WHEN savedAt > lastReviewedAt THEN savedAt ELSE lastReviewedAt END DESC")
    fun getSavedOrLearnedWords(): Flow<List<VocabWord>>

    @Query("SELECT * FROM vocab_words WHERE id = :id LIMIT 1")
    suspend fun getWordById(id: Long): VocabWord?

    @Query("SELECT COUNT(*) FROM vocab_words")
    suspend fun getWordCount(): Int

    @Query("SELECT * FROM vocab_words WHERE isSaved = 1 OR isLearned = 1 OR isCustom = 1 OR confidenceRating > 0")
    suspend fun getUserModifiedWords(): List<VocabWord>

    @Query("SELECT word FROM vocab_words")
    suspend fun getAllExistingWordNames(): List<String>

    @Query("SELECT * FROM vocab_words")
    suspend fun getAllWordsSnapshot(): List<VocabWord>

    @Query("UPDATE vocab_words SET category = :category, difficulty = :difficulty WHERE LOWER(TRIM(word)) = LOWER(TRIM(:word)) AND isCustom = 0")
    suspend fun updateWordCategoryAndDifficulty(word: String, category: String, difficulty: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabWord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<VocabWord>)

    @Update
    suspend fun updateWord(word: VocabWord)

    @Query("UPDATE vocab_words SET isSaved = :isSaved, savedAt = :savedAt WHERE id = :id")
    suspend fun updateSavedStatus(id: Long, isSaved: Boolean, savedAt: Long)

    @Query("UPDATE vocab_words SET isLearned = :isLearned, lastReviewedAt = :reviewedAt, reviewCount = reviewCount + 1 WHERE id = :id")
    suspend fun updateLearnedStatus(id: Long, isLearned: Boolean, reviewedAt: Long)

    @Query("UPDATE vocab_words SET confidenceRating = :rating, lastReviewedAt = :reviewedAt, reviewCount = reviewCount + 1 WHERE id = :id")
    suspend fun updateConfidenceRating(id: Long, rating: Int, reviewedAt: Long)

    @Query("UPDATE vocab_words SET lastReviewedAt = :reviewedAt, reviewCount = reviewCount + 1 WHERE id = :id")
    suspend fun recordWordReview(id: Long, reviewedAt: Long)

    @Query("SELECT COUNT(*) FROM vocab_words WHERE lastReviewedAt >= :startOfDay")
    fun getReviewedTodayCount(startOfDay: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM vocab_words WHERE isLearned = 1")
    fun getLearnedWordsCount(): Flow<Int>

    @Query("SELECT * FROM user_learning_goal WHERE id = 1 LIMIT 1")
    fun getLearningGoal(): Flow<UserLearningGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLearningGoal(goal: UserLearningGoal)

    @Query("DELETE FROM vocab_words WHERE id = :id")
    suspend fun deleteWordById(id: Long)

    @Query("DELETE FROM vocab_words WHERE isCustom = 0")
    suspend fun clearDefaultWords()

    @Query("SELECT * FROM daily_learning_stats WHERE date = :date LIMIT 1")
    fun getDailyStats(date: String): Flow<DailyStats?>

    @Query("SELECT * FROM daily_learning_stats WHERE date = :date LIMIT 1")
    suspend fun getDailyStatsSnapshot(date: String): DailyStats?

    @Query("SELECT * FROM daily_learning_stats ORDER BY date DESC")
    fun getAllDailyStats(): Flow<List<DailyStats>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyStats(stats: DailyStats)
}
