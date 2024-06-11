package com.advancednotes.ui.screens.single_note.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.advancednotes.R
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.textfields.AdvancedTextField

@Composable
fun RenameRecordDialog(
    renameRecordDialogState: RenameRecordDialogState,
    onConfirm: (newRecordName: String) -> Unit,
    onCloseDialog: () -> Unit
) {
    var textFieldValue: TextFieldValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    LaunchedEffect(renameRecordDialogState) {
        if (renameRecordDialogState.noteRecord != null) {
            textFieldValue = textFieldValue.copy(
                text = renameRecordDialogState.noteRecord.fileName
            )
        }
    }

    MyAlertDialog(
        opened = renameRecordDialogState.opened,
        onDismissRequest = {
            onCloseDialog()
        },
        confirmButton = {
            MyTextButton(
                text = stringResource(id = R.string.continue_button),
                onClick = {
                    onConfirm(textFieldValue.text)
                    onCloseDialog()
                }
            )
        },
        dismissButton = {
            MyTextButton(
                text = stringResource(id = R.string.cancel_button),
                onClick = {
                    onCloseDialog()
                }
            )
        },
        title = stringResource(id = R.string.rename),
        content = {
            Column {
                AdvancedTextField(
                    label = stringResource(id = R.string.note_record),
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    showCounter = true
                )
            }
        })
}