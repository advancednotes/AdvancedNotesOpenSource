package com.advancednotes.ui.components.single_note.advanced_content.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.advancednotes.ui.components.textfields.EmptyTextField

@Composable
fun AdvancedContentListItem(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (isFocused: Boolean) -> Unit,
    isTheLastItem: Boolean,
    onNextFocus: () -> Unit,
    onItemRemoved: () -> Unit,
    onNewItemAdded: () -> Unit,
    showReorganizeButtons: Boolean,
    onReorganizeUp: () -> Unit,
    onReorganizeDown: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmptyTextField(
            value = value,
            onValueChange = { newTextFieldValue ->
                onValueChange(newTextFieldValue)
            },
            modifier = Modifier
                .weight(1f),
            textFieldModifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChanged.invoke(it.isFocused)
                }
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                        if (value.text.isEmpty()) {
                            onItemRemoved()
                        }

                        true
                    } else if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                        /* USE FOR NEXT FOCUS
                        if (isTheLastItem) {
                            if (value.text.isNotEmpty()) {
                                onNewItemAdded()
                            }
                        } else {
                            onNextFocus()
                        }*/

                        if (value.text.isNotEmpty()) {
                            onNewItemAdded()
                        }

                        true
                    } else {
                        false
                    }
                }
                .padding(all = 5.dp),
            textStyle = MaterialTheme.typography.bodySmall,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = null,
                    modifier = Modifier.size(7.dp)
                )
            }
        )

        if (showReorganizeButtons) {
            AdvancedContentReorganizeButtonsList(
                onReorganizeUp = onReorganizeUp,
                onReorganizeDown = onReorganizeDown
            )
        }
    }
}