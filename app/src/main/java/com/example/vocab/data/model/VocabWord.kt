package com.example.vocab.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocab_words")
data class VocabWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val meaning: String,
    val example: String,
    val category: String,
    val difficulty: String,
    val isSaved: Boolean = false,
    val savedAt: Long = 0L,
    val isCustom: Boolean = false,
    val isLearned: Boolean = false,
    val confidenceRating: Int = 0, // 0 = unrated, 1 to 5
    val lastReviewedAt: Long = 0L,
    val reviewCount: Int = 0
)

@Entity(tableName = "user_learning_goal")
data class UserLearningGoal(
    @PrimaryKey
    val id: Int = 1,
    val dailyGoal: Int = 10
)

enum class DifficultyLevel(val displayName: String) {
    EASY("Easy"),
    INTERMEDIATE("Intermediate"),
    HARD("Hard");

    companion object {
        fun fromString(value: String): DifficultyLevel {
            return entries.firstOrNull { it.displayName.equals(value, ignoreCase = true) } ?: EASY
        }
    }
}

object VocabCategories {
    const val ALL = "All Categories"
    const val CORPORATE = "Corporate"
    const val IDIOMS = "Idioms"
    const val MEDICAL = "Medical terms"
    const val BUSINESS = "Business terms"
    const val TECHNICAL = "Technical terms"
    const val STARTUP = "Startup terms"
    const val GEN_Z = "Gen Z terms"
    const val HISTORY = "History terms"
    const val DOCTOR_SPECIALIZED = "Doctor & Specialized"
    const val GENERAL_ENGLISH = "General English"
    const val OXFORD_DICTIONARY = "Oxford Dictionary"

    val list = listOf(
        ALL,
        CORPORATE,
        IDIOMS,
        MEDICAL,
        BUSINESS,
        TECHNICAL,
        STARTUP,
        GEN_Z,
        HISTORY,
        DOCTOR_SPECIALIZED,
        GENERAL_ENGLISH,
        OXFORD_DICTIONARY
    )
}
