package com.example.vocab.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_learning_stats")
data class DailyStats(
    @PrimaryKey
    val date: String, // Formatted as YYYY-MM-DD
    val reviewsScrolled: Int = 0,
    val wordsLearned: Int = 0,
    val goalAchieved: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
