package com.advancednotes.ui.components.textfields

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.R

@Composable
fun EmailTextField(
    value: TextFieldValue,
    onValueChange: (newValue: TextFieldValue) -> Unit,
    supportingText: String = "",
    isError: Boolean = false,
    isLoadingEmail: Boolean = false
) {
    AdvancedTextField(
        label = stringResource(id = R.string.email_label),
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        supportingText = supportingText,
        isError = isError,
        singleLine = true,
        trailingIcon = if (isLoadingEmail) {
            {
                Icon(
                    imageVector = Icons.Default.Downloading,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        keyboardOptions = KeyboardOptions.Default
    )
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun EmailTextFieldPreviewLight() {
    EmailTextField(
        value = TextFieldValue("Preview"),
        onValueChange = {}
    )
}