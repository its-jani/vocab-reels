package com.example.vocab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vocab.data.model.DailyStats
import com.example.vocab.data.model.UserLearningGoal
import com.example.vocab.data.model.VocabWord

@Database(entities = [VocabWord::class, UserLearningGoal::class, DailyStats::class], version = 3, exportSchema = false)
abstract class VocabDatabase : RoomDatabase() {
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile
        private var INSTANCE: VocabDatabase? = null

        fun getDatabase(context: Context): VocabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VocabDatabase::class.java,
                    "vocab_reels_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
