package com.advancednotes.ui.components.single_note.advanced_content.textfield

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.AdvancedContentUIObject
import com.advancednotes.ui.components.textfields.EmptyTextField

@Composable
fun AdvancedContentText(
    content: AdvancedContentUIObject.MyText,
    onContentChange: (content: AdvancedContentUIObject.MyText) -> Unit,
    focusRequester: FocusRequester,
    onFocused: () -> Unit,
    onTextFieldRemoved: () -> Unit
) {
    var textFieldValue: TextFieldValue by remember {
        mutableStateOf(content.value.textFieldValue)
    }

    fun onValueChange(newTextFieldValue: TextFieldValue) {
        onContentChange(
            content.copy(
                value = content.value.copy(textFieldValue = newTextFieldValue)
            )
        )
    }

    LaunchedEffect(content) {
        textFieldValue = content.value.textFieldValue
    }

    EmptyTextField(
        value = textFieldValue,
        onValueChange = { newTextFieldValue ->
            textFieldValue = newTextFieldValue
            if (newTextFieldValue.text.isNotEmpty()) {
                onValueChange(newTextFieldValue)
            }
        },
        modifier = Modifier,
        textFieldModifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) onFocused()
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                    if (textFieldValue.text.isEmpty()) {
                        onTextFieldRemoved()
                    }

                    true
                } else {
                    false
                }
            }
            .padding(all = 7.dp)
    )
}