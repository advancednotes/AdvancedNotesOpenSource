package com.advancednotes.ui.screens.single_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.usecases.TextToSpeechUseCase
import com.advancednotes.domain.usecases.impl.TTSCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextToSpeechViewModel @Inject constructor(
    private val textToSpeechUseCase: TextToSpeechUseCase
) : ViewModel() {

    private val _currentUtteranceId = MutableStateFlow("")
    val currentUtteranceId: StateFlow<String> = _currentUtteranceId.asStateFlow()

    fun setUtteranceId(utteranceId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _currentUtteranceId.value = utteranceId
        }
    }

    fun ttsSpeaking(): Boolean = textToSpeechUseCase.ttsSpeaking()

    fun isTheSameUtteranceId(utteranceId: String): Boolean =
        utteranceId == _currentUtteranceId.value

    fun ttsSpeak(text: String, utteranceId: String, callback: TTSCallback) {
        viewModelScope.launch(Dispatchers.Default) {
            textToSpeechUseCase.ttsSpeak(text, utteranceId, callback)
        }
    }

    fun ttsStop(onStop: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            textToSpeechUseCase.ttsStop(onStop)
        }
    }
}