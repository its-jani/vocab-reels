package com.example.vocab.ui.components

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import com.example.vocab.data.model.VocabCategories

object CategoryBadgeHelper {

    fun getCategoryColor(category: String): Color {
        return when (category) {
            VocabCategories.TECHNICAL -> CategoryTechnical
            VocabCategories.MEDICAL -> CategoryMedical
            VocabCategories.BUSINESS -> CategoryBusiness
            VocabCategories.STARTUP -> CategoryStartup
            VocabCategories.GENERAL_ENGLISH -> CategoryGeneral
            VocabCategories.HISTORY -> CategoryHistory
            VocabCategories.CORPORATE -> CategoryCorporate
            VocabCategories.IDIOMS -> CategoryIdioms
            VocabCategories.DOCTOR_SPECIALIZED -> CategoryDoctor
            VocabCategories.GEN_Z -> CategoryGenZ
            else -> NeonCyan
        }
    }

    fun getDifficultyColor(difficulty: String): Color {
        return when (difficulty.lowercase()) {
            "easy" -> DifficultyEasy
            "intermediate" -> DifficultyIntermediate
            "hard" -> DifficultyHard
            else -> MutedText
        }
    }
}
