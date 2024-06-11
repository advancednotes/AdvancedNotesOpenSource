package com.advancednotes.ui.components.textfields

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.advancednotes.R
import com.advancednotes.ui.components.buttons.MyTextButton

@Composable
fun PasswordTextField(
    value: TextFieldValue,
    onValueChange: (newValue: TextFieldValue) -> Unit,
    label: String? = null,
    onClickForgotPassword: (() -> Unit)? = null
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
    ) {
        AdvancedTextField(
            label = label ?: stringResource(id = R.string.password_label),
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                IconToggleButton(
                    checked = passwordVisible,
                    onCheckedChange = {
                        passwordVisible = it
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default
        )

        if (onClickForgotPassword != null) {
            MyTextButton(
                text = stringResource(id = R.string.forgot_password),
                onClick = {
                    onClickForgotPassword()
                },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun PasswordTextFieldPreviewLight() {
    PasswordTextField(
        value = TextFieldValue("Preview"),
        onValueChange = {},
        onClickForgotPassword = {}
    )
}