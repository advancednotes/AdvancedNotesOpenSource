package com.advancednotes.ui.components.textfields

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.texts.MyText

@Composable
fun EmptyTextField(
    value: TextFieldValue,
    onValueChange: (newValue: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: String = "",
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showCounter: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) {
    var isFocused: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.invoke()

                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    CompositionLocalProvider(
                        LocalTextSelectionColors provides TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                    ) {
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            modifier = textFieldModifier
                                .fillMaxWidth()
                                .onFocusChanged {
                                    isFocused = it.isFocused
                                },
                            enabled = enabled,
                            readOnly = readOnly,
                            textStyle = LocalTextStyle.current.copy(
                                color = if (!isError) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else MaterialTheme.colorScheme.error,
                                background = Color.Transparent,
                                fontFamily = textStyle.fontFamily,
                                fontWeight = textStyle.fontWeight,
                                fontSize = textStyle.fontSize,
                                lineHeight = textStyle.lineHeight,
                                letterSpacing = textStyle.letterSpacing,
                            ),
                            keyboardOptions = keyboardOptions,
                            keyboardActions = keyboardActions,
                            singleLine = singleLine,
                            minLines = minLines,
                            maxLines = maxLines,
                            visualTransformation = visualTransformation,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                        )
                    }

                    if (placeholder != null && value.text.isEmpty()) {
                        MyText(
                            text = placeholder,
                            modifier = textFieldModifier,
                            color = if (!isError) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            } else MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                            textStyle = textStyle,
                        )
                    }
                }
            }
        }

        if (supportingText.isNotEmpty() || showCounter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (supportingText.isNotEmpty()) {
                    MyText(
                        text = supportingText,
                        color = if (!isError) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else MaterialTheme.colorScheme.error,
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                }

                if (showCounter) {
                    Spacer(modifier = Modifier.weight(1f))
                    MyText(
                        text = value.text.length.toString(),
                        modifier = Modifier
                            .padding(start = 16.dp),
                        color = if (!isError) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else MaterialTheme.colorScheme.error,
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun EmptyTextFieldPreviewLight() {
    EmptyTextField(
        value = TextFieldValue(text = "Preview"),
        onValueChange = {}
    )
}