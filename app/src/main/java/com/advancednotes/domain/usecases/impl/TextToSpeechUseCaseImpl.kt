package com.advancednotes.domain.usecases.impl

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.advancednotes.domain.usecases.TextToSpeechUseCase
import java.util.Locale
import javax.inject.Inject

class TextToSpeechUseCaseImpl @Inject constructor(
    private val context: Context,
) : TextToSpeechUseCase, TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result: Int = tts.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // El idioma no está disponible o no es compatible
            }
        } else {
            // Falla la inicialización de TextToSpeech
        }
    }

    override fun ttsSpeaking(): Boolean = tts.isSpeaking

    override fun ttsSpeak(text: String, utteranceId: String?, callback: TTSCallback?) {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
                callback?.invoke(TTSStatus.START)
            }

            override fun onDone(utteranceId: String) {
                callback?.invoke(TTSStatus.DONE)
            }

            @Deprecated(
                "Deprecated in Java",
                ReplaceWith("onError(utteranceId: String, errorCode: Int)")
            )
            override fun onError(utteranceId: String?) {
                callback?.invoke(TTSStatus.ERROR)
            }

            override fun onError(utteranceId: String, errorCode: Int) {
                callback?.invoke(TTSStatus.ERROR)
            }
        })

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    override fun ttsStop(onStop: () -> Unit) {
        tts.stop()
        onStop()
    }
}

enum class TTSStatus {
    START,
    DONE,
    ERROR
}

typealias TTSCallback = ((ttsStatus: TTSStatus) -> Unit)