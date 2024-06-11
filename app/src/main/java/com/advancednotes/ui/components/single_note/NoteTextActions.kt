package com.advancednotes.ui.components.single_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.domain.usecases.impl.TTSStatus
import com.advancednotes.ui.components.buttons.MyIconButton
import com.advancednotes.ui.screens.single_note.TextToSpeechViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteTextActions(
    text: String,
    ttsUtteranceId: String,
    onCleanText: () -> Unit,
    textToSpeechViewModel: TextToSpeechViewModel,
    modifier: Modifier = Modifier,
) {
    val scope: CoroutineScope = rememberCoroutineScope()

    val ttsCurrentUtteranceId: String by textToSpeechViewModel.currentUtteranceId.collectAsState()

    if (text.isNotEmpty()) {
        Row(
            modifier = modifier
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*MyIconButton(
                icon = Icons.Default.Mic,
                onClick = {
                    textToSpeechViewModel...
                },
                contentDescription = stringResource(id = R.string.cd_sst)
            )*/

            MyIconButton(
                icon = Icons.Default.CleaningServices,
                onClick = {
                    onCleanText()
                },
                contentDescription = stringResource(id = R.string.cd_clean_text)
            )

            MyIconButton(
                icon = if (ttsUtteranceId == ttsCurrentUtteranceId) {
                    Icons.Default.Stop
                } else Icons.Default.RecordVoiceOver,
                onClick = {
                    var startSpeak: Boolean = false

                    if (textToSpeechViewModel.ttsSpeaking()) {
                        if (!textToSpeechViewModel.isTheSameUtteranceId(ttsUtteranceId)) {
                            startSpeak = true
                        }

                        textToSpeechViewModel.ttsStop(
                            onStop = {
                                scope.launch {
                                    textToSpeechViewModel.setUtteranceId("")
                                }
                            }
                        )
                    } else {
                        startSpeak = true
                    }

                    if (startSpeak) {
                        textToSpeechViewModel.ttsSpeak(
                            text = text,
                            utteranceId = ttsUtteranceId,
                            callback = { ttsStatus ->
                                when (ttsStatus) {
                                    TTSStatus.START -> textToSpeechViewModel.setUtteranceId(
                                        ttsUtteranceId
                                    )

                                    TTSStatus.DONE -> textToSpeechViewModel.setUtteranceId("")
                                    TTSStatus.ERROR -> textToSpeechViewModel.setUtteranceId("")
                                }
                            })
                    }
                },
                contentDescription = stringResource(id = R.string.cd_tts)
            )
        }
    }
}