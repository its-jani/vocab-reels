package com.example.vocab.util

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentlySpeakingWord = MutableStateFlow<String?>(null)
    val currentlySpeakingWord: StateFlow<String?> = _currentlySpeakingWord.asStateFlow()

    private var currentSpeed: Float = 1.0f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("TtsManager", "US English not directly supported, trying default Locale")
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(currentSpeed)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingWord.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingWord.value = null
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                    _currentlySpeakingWord.value = null
                }
            })
            isInitialized = true
        } else {
            Log.e("TtsManager", "Initialization of TextToSpeech failed")
        }
    }

    fun setSpeechRate(rate: Float) {
        currentSpeed = rate
        if (isInitialized) {
            tts?.setSpeechRate(rate)
        }
    }

    fun getSpeechRate(): Float = currentSpeed

    fun speak(word: String, rate: Float = currentSpeed) {
        if (!isInitialized || tts == null) {
            return
        }
        stop()
        setSpeechRate(rate)
        _currentlySpeakingWord.value = word
        _isSpeaking.value = true
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance_${System.currentTimeMillis()}")
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, params, "vocab_word_$word")
    }

    fun stop() {
        if (isInitialized && tts != null) {
            tts?.stop()
        }
        _isSpeaking.value = false
        _currentlySpeakingWord.value = null
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
