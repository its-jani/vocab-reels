package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.vocab.data.local.VocabDatabase
import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.repository.VocabRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Vocab Reels", appName)
  }

  @Test
  fun `verify oxford dictionary asset exists and loads thousands of words`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = Room.inMemoryDatabaseBuilder(context, VocabDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    val repo = VocabRepository(db.vocabDao(), context)

    // Verify initial state is empty
    assertEquals(0, db.vocabDao().getWordCount())

    // Initialize Oxford dictionary
    repo.initializeDefaultWordsIfEmpty()

    val count = db.vocabDao().getWordCount()
    assertTrue("Dictionary should contain thousands of words (found $count)", count >= 4000)

    // Verify Oxford category exists and is populated
    val categories = VocabCategories.list
    assertTrue("Oxford Dictionary category must be present", categories.contains(VocabCategories.OXFORD_DICTIONARY))

    db.close()
  }
}
