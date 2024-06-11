package com.advancednotes.ui.components.single_note.records

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.ui.components.MyDropdownMenu
import com.advancednotes.ui.components.cards.AdvancedStyledCard
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.screens.single_note.RecordsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Record(
    record: NoteFile,
    contextMenuItemData: List<ContextMenuItem>,
    recordsViewModel: RecordsViewModel
) {
    val context: Context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val recordPlayingIdentifier: String by recordsViewModel.recordPlayingIdentifier.collectAsState()

    var fileUri: Uri? by remember { mutableStateOf(null) }

    var sliderRange: ClosedFloatingPointRange<Float> by remember { mutableStateOf(0f..100f) }
    var sliderValue: Float by remember { mutableFloatStateOf(0f) }

    var recordProgress: String by remember { mutableStateOf("00:00") }
    var recordDuration: String by remember { mutableStateOf("00:00") }

    var recordDropDownExpanded: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fileUri = recordsViewModel.getFileUri(context, record)

        val durationMills: Float? = fileUri?.let { recordsViewModel.getDurationMills(it).toFloat() }

        fun getSliderRange(durationMillsAux: Float): ClosedFloatingPointRange<Float> {
            val min: Float = 0f
            val max: Float = durationMillsAux

            recordDuration = recordsViewModel.formatProgress(max)

            return min..max
        }

        if (durationMills != null) {
            sliderRange = getSliderRange(durationMills)
        }
    }

    LaunchedEffect(recordDropDownExpanded) {
        if (recordDropDownExpanded) {
            if (recordsViewModel.isPlaying()) {
                if (recordsViewModel.isTheSameRecord(record.fileUUID)) {
                    recordsViewModel.stopRecord(
                        onStop = {
                            scope.launch {
                                recordsViewModel.setRecordPlayingIdentifier("")
                            }
                        }
                    )
                }
            }
        }
    }

    AdvancedStyledCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        recordDropDownExpanded = true
                    }
                )
                .padding(all = 10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                MyText(
                    modifier = Modifier.weight(1f),
                    text = record.fileName
                )

                IconButton(
                    onClick = {
                        var playRecord: Boolean = false

                        if (recordsViewModel.isPlaying()) {
                            if (!recordsViewModel.isTheSameRecord(record.fileUUID)) {
                                playRecord = true
                            }

                            recordsViewModel.stopRecord(
                                onStop = {
                                    scope.launch {
                                        recordsViewModel.setRecordPlayingIdentifier("")
                                    }
                                }
                            )
                        } else {
                            playRecord = true
                        }

                        if (playRecord && fileUri != null) {
                            recordsViewModel.playRecord(
                                fileUri = fileUri!!,
                                progress = sliderValue,
                                onStart = {
                                    scope.launch {
                                        recordsViewModel.setRecordPlayingIdentifier(record.fileUUID)
                                    }
                                },
                                onComplete = {
                                    scope.launch {
                                        recordsViewModel.setRecordPlayingIdentifier("")
                                        sliderValue = 0f
                                        recordProgress = recordsViewModel.formatProgress(0f)
                                    }
                                },
                                onProgress = { currentProgress ->
                                    scope.launch {
                                        sliderValue = currentProgress
                                        recordProgress =
                                            recordsViewModel.formatProgress(currentProgress)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (recordPlayingIdentifier == record.fileUUID) {
                            Icons.Default.Stop
                        } else Icons.Default.PlayArrow,
                        contentDescription = if (recordPlayingIdentifier == record.fileUUID) {
                            stringResource(id = R.string.cd_stop_record)
                        } else stringResource(id = R.string.cd_play_record),
                        modifier = Modifier
                            .size(ButtonDefaults.IconSize)
                    )
                }
            }

            Slider(
                value = sliderValue,
                valueRange = sliderRange,
                onValueChange = { currentValue ->
                    sliderValue = currentValue
                    recordProgress = recordsViewModel.formatProgress(currentValue)

                    if (recordsViewModel.isTheSameRecord(record.fileUUID)) {
                        recordsViewModel.seekTo(progress = currentValue)
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                MyText(
                    modifier = Modifier.weight(1f),
                    text = recordProgress,
                    textAlign = TextAlign.Start,
                    textStyle = MaterialTheme.typography.labelSmall
                )
                MyText(
                    modifier = Modifier.weight(1f),
                    text = recordDuration,
                    textAlign = TextAlign.End,
                    textStyle = MaterialTheme.typography.labelSmall
                )
            }
        }
    }

    MyDropdownMenu(
        expanded = recordDropDownExpanded,
        onDismissRequest = {
            recordDropDownExpanded = false
        }
    ) {
        contextMenuItemData.forEach { dropdownItem ->
            DropdownMenuItem(
                text = { Text(text = dropdownItem.text) },
                onClick = {
                    dropdownItem.onClick()
                    recordDropDownExpanded = false
                }
            )
        }
    }
}