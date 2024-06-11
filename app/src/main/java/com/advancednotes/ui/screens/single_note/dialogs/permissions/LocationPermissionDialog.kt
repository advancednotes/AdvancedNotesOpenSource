package com.advancednotes.ui.screens.single_note.dialogs.permissions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.advancednotes.R
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.texts.MyText

@Composable
fun LocationPermissionDialog(
    opened: Boolean,
    onConfirm: () -> Unit,
    onCloseDialog: () -> Unit
) {
    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        icon = Icons.Filled.LocationOn,
        title = stringResource(id = R.string.permissions_dialog_title),
        content = {
            MyText(
                text = stringResource(id = R.string.location_permission_rationale),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            MyTextButton(
                text = stringResource(id = R.string.permission_button),
                onClick = {
                    onConfirm()
                    onCloseDialog()
                }
            )
        }
    )
}