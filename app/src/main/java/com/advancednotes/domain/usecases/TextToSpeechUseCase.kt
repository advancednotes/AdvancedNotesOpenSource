package com.advancednotes.domain.usecases

import com.advancednotes.domain.usecases.impl.TTSCallback

interface TextToSpeechUseCase {

    fun ttsSpeaking(): Boolean
    fun ttsSpeak(text: String, utteranceId: String? = null, callback: TTSCallback? = null)
    fun ttsStop(onStop: () -> Unit)
}