package com.advancednotes.ui.components.textfields

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.cards.MyElevatedCard
import com.advancednotes.ui.components.texts.MyText

@Composable
fun AdvancedTextField(
    value: TextFieldValue,
    onValueChange: (newValue: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: String = "",
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showCounter: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = RoundedCornerShape(100),
    borderWidth: Dp = 1.dp
) {
    var isFocused: Boolean by remember { mutableStateOf(false) }

    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isFocused) 0.9f else 0.6f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isFocused) 0.9f else 0.6f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isFocused) 0.9f else 0.6f)
    )

    val borderColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isFocused) 1f else 0.8f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isFocused) 1f else 0.8f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isFocused) 1f else 0.8f)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val borderModifier = if (borderWidth != 0.dp) {
            Modifier
                .border(
                    width = borderWidth,
                    brush = Brush.linearGradient(colors = borderColors),
                    shape = shape
                )
        } else Modifier

        val backgroundModifier = if (isError) {
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.errorContainer
                )
        } else {
            Modifier
                .background(
                    brush = Brush.linearGradient(colors = backgroundColors)
                )
        }

        MyElevatedCard(
            shape = shape
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        shape = shape
                    )
                    .then(borderModifier)
                    .then(backgroundModifier)
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = if (label != null) 28.dp else 18.dp,
                        top = 14.dp,
                        end = 18.dp,
                        bottom = 14.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIcon?.invoke()

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (label != null) {
                            MyText(
                                text = label,
                                modifier = if (isFocused) {
                                    Modifier
                                } else {
                                    Modifier
                                        .alpha(0.7f)
                                },
                                textStyle = MaterialTheme.typography.labelSmall
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(all = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            CompositionLocalProvider(
                                LocalTextSelectionColors provides TextSelectionColors(
                                    handleColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.4f
                                    )
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
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.5f
                                    ),
                                    textStyle = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }

                    trailingIcon?.invoke()
                }
            }
        }

        if (supportingText.isNotEmpty() || showCounter) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (supportingText.isNotEmpty()) {
                    MyText(
                        text = supportingText,
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                }

                if (showCounter) {
                    Spacer(modifier = Modifier.weight(1f))
                    MyText(
                        text = value.text.length.toString(),
                        modifier = Modifier
                            .padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
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
fun AdvancedTextFieldPreviewLight() {
    AdvancedTextField(
        value = TextFieldValue(text = "Preview"),
        onValueChange = {}
    )
}