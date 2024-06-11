package com.advancednotes.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.ui.components.buttons.MyButton
import com.advancednotes.ui.components.textfields.PasswordTextField

@Composable
fun ChangeUserPassword(
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
    ) {
        var newPassword: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
        var newPasswordValidate: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

        val saveButtonEnabled: Boolean by remember { mutableStateOf(false) }

        PasswordTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it
            },
            label = stringResource(id = R.string.user_change_password_label)
        )

        PasswordTextField(
            value = newPasswordValidate,
            onValueChange = {
                newPasswordValidate = it
            },
            label = stringResource(id = R.string.user_change_password_verify_label)
        )

        MyButton(
            text = stringResource(id = R.string.save_button),
            onClick = {

            },
            modifier = Modifier.align(Alignment.End),
            enabled = saveButtonEnabled
        )
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun ChangeUserPasswordPreviewLight() {
    ChangeUserPassword()
}