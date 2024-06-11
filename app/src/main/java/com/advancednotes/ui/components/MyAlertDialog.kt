package com.advancednotes.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.advancednotes.R
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyAlertDialog(
    opened: Boolean,
    onDismissRequest: () -> Unit,
    icon: ImageVector? = null,
    title: String? = null,
    content: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit = {},
    dismissButton: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties()
) {
    if (opened) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            icon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            } else null,
            title = if (title != null) {
                {
                    MyText(
                        text = title,
                        textStyle = MaterialTheme.typography.titleSmall
                    )
                }
            } else null,
            text = content,
            properties = properties
        )
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyAlertDialogPreviewLight() {
    MyAlertDialog(
        opened = true,
        onDismissRequest = { },
        confirmButton = { },
        dismissButton = { },
        icon = Icons.Filled.Map,
        title = stringResource(id = R.string.trash_note_title),
        content = { MyText(text = stringResource(id = R.string.trash_note_text)) }
    )
}