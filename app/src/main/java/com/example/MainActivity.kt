package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.theme.VocabReelsTheme
import com.example.vocab.ui.VocabReelsApp
import com.example.vocab.ui.VocabViewModel

class MainActivity : ComponentActivity() {

    companion object {
        /**
         * Set by the Streak & Goal Tracker home screen widget's one-tap quick
         * action to open the app directly on today's activity panel.
         */
        const val EXTRA_OPEN_TODAYS_ACTIVITY = "com.example.vocab.widget.OPEN_TODAYS_ACTIVITY"
    }

    private val vocabViewModel: VocabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocabReelsTheme {
                VocabReelsApp(viewModel = vocabViewModel)
            }
        }
        handleWidgetQuickAction(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleWidgetQuickAction(intent)
    }

    /** Widget quick action: jump straight to the dashboard with today's streak & goal. */
    private fun handleWidgetQuickAction(intent: Intent?) {
        if (intent?.getBooleanExtra(EXTRA_OPEN_TODAYS_ACTIVITY, false) == true) {
            intent.removeExtra(EXTRA_OPEN_TODAYS_ACTIVITY)
            vocabViewModel.setMainMenuVisible(true)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
