package com.advancednotes.ui.screens.notes.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.advancednotes.R
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.texts.MyText

@Composable
fun EmptyTrashDialog(
    opened: Boolean,
    onConfirm: () -> Unit,
    onCloseDialog: () -> Unit
) {
    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        confirmButton = {
            MyTextButton(
                text = stringResource(id = R.string.continue_button),
                onClick = {
                    onConfirm()
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
        title = stringResource(id = R.string.empty_trash_title),
        content = { MyText(text = stringResource(id = R.string.empty_trash_text)) }
    )
}