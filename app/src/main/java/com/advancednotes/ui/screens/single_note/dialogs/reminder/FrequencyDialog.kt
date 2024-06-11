package com.advancednotes.ui.screens.single_note.dialogs.reminder

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.advancednotes.R
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.Frequency
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.Spinner
import com.advancednotes.utils.notes.NotesHelper.getReminderFrequencyText

@Composable
fun FrequencyDialog(
    opened: Boolean,
    frequency: Frequency,
    onChangeFrequency: (frequency: Frequency) -> Unit,
    onCloseDialog: () -> Unit
) {
    val context: Context = LocalContext.current

    var reminderFrequencyText: String by remember { mutableStateOf("") }

    LaunchedEffect(frequency) {
        reminderFrequencyText = getReminderFrequencyText(context, frequency)
    }

    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        title = stringResource(id = R.string.reminder_recurrent_title),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items: List<ContextMenuItem> = Frequency.values().map { frequencyAux ->
                    ContextMenuItem(
                        text = getReminderFrequencyText(context, frequencyAux),
                        onClick = {
                            onChangeFrequency(frequencyAux)
                            onCloseDialog()
                        }
                    )
                }

                Spinner(
                    items = items,
                    selectedItemText = reminderFrequencyText
                )
            }
        },
    )
}