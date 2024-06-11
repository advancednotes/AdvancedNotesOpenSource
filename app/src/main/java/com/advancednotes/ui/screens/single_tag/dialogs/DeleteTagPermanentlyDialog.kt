package com.advancednotes.ui.screens.single_tag.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.advancednotes.R
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.texts.MyText

@Composable
fun DeleteTagPermanentlyDialog(
    deleteTagDialogOpened: Boolean,
    tagName: String,
    onConfirm: () -> Unit,
    onCloseDialog: () -> Unit
) {
    MyAlertDialog(
        opened = deleteTagDialogOpened,
        onDismissRequest = {
            onCloseDialog()
        },
        title = stringResource(id = R.string.delete_note_tag_title),
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
        content = {
            MyText(
                text = stringResource(
                    id = R.string.delete_note_tag_text,
                    tagName
                )
            )
        }
    )
}