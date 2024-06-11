package com.advancednotes.ui.components.single_note.advanced_content.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.AdvancedContentListItemUI
import com.advancednotes.domain.models.AdvancedContentUIObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AdvancedContentList(
    content: AdvancedContentUIObject.MyList,
    onContentChange: (content: AdvancedContentUIObject.MyList) -> Unit,
    onListRemoved: () -> Unit,
    onSomeItemFocused: () -> Unit,
    listIsFocused: Boolean
) {
    val scope: CoroutineScope = rememberCoroutineScope()

    var focusedIndex: Int by remember { mutableIntStateOf(-1) }

    fun setFocusedByIndex(index: Int) {
        focusedIndex = index
        onSomeItemFocused()
    }

    fun setNextFocus(index: Int) {
        val nextIndex = index + 1
        val listHasNextIndex = nextIndex <= content.value.lastIndex

        if (listHasNextIndex) {
            setFocusedByIndex(nextIndex)
        }
    }

    fun onValueChange(index: Int, newTextFieldValue: TextFieldValue) {
        onContentChange(
            content.copy(
                value = content.value.apply {
                    this[index].textFieldValue = newTextFieldValue
                }
            )
        )
    }

    fun onItemRemoved(index: Int) {
        val listAux: MutableList<AdvancedContentListItemUI> = content.value.toMutableList()

        listAux.removeAt(index)

        val previousIndex = index - 1
        val listHasPreviousIndex = previousIndex >= 0 && previousIndex < listAux.size

        if (listHasPreviousIndex) {
            setFocusedByIndex(previousIndex)
        }

        if (listAux.isNotEmpty()) {
            onContentChange(
                content.copy(value = listAux)
            )
        } else {
            onListRemoved()
        }
    }

    fun onNewItemAdded(position: Int) {
        val listAux: MutableList<AdvancedContentListItemUI> = content.value.toMutableList()
        listAux.add(position, AdvancedContentListItemUI(TextFieldValue(text = "")))

        setFocusedByIndex(position)

        onContentChange(
            content.copy(value = listAux)
        )
    }

    fun onReorganizeUp(index: Int) {
        val previousIndex = index - 1
        val listHasPreviousIndex = previousIndex >= 0 && previousIndex < content.value.size

        if (listHasPreviousIndex) {
            val listAux: MutableList<AdvancedContentListItemUI> = content.value.toMutableList()
            val tempItem = listAux[index]

            listAux.removeAt(index)
            listAux.add(previousIndex, tempItem)

            setFocusedByIndex(previousIndex)

            onContentChange(
                content.copy(value = listAux)
            )
        }
    }

    fun onReorganizeDown(index: Int) {
        val nextIndex = index + 1
        val listHasNextIndex = nextIndex <= content.value.lastIndex

        if (listHasNextIndex) {
            val listAux: MutableList<AdvancedContentListItemUI> = content.value.toMutableList()
            val tempItem = listAux[index]

            listAux.removeAt(index)
            listAux.add(nextIndex, tempItem)

            setFocusedByIndex(nextIndex)

            onContentChange(
                content.copy(value = listAux)
            )
        }
    }

    LaunchedEffect(listIsFocused) {
        if (listIsFocused) {
            if (focusedIndex == -1) {
                focusedIndex = content.value.lastIndex
            }
        } else {
            focusedIndex = -1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp)
    ) {
        content.value.forEachIndexed { index, item ->
            var textFieldValue: TextFieldValue by remember {
                mutableStateOf(item.textFieldValue)
            }

            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(item) {
                textFieldValue = item.textFieldValue
            }

            AdvancedContentListItem(
                value = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    val newTextFieldValueEscaped =
                        newTextFieldValue.copy(newTextFieldValue.text.replace("\n", ""))
                    textFieldValue = newTextFieldValueEscaped

                    if (newTextFieldValue.text.isNotEmpty()) {
                        onValueChange(index, newTextFieldValueEscaped)
                    }
                },
                focusRequester = focusRequester,
                onFocusChanged = { isFocused ->
                    if (isFocused) setFocusedByIndex(index)
                },
                isTheLastItem = index == content.value.lastIndex,
                onNextFocus = {
                    setNextFocus(index)
                },
                onItemRemoved = {
                    scope.launch {
                        onItemRemoved(index)
                    }
                },
                onNewItemAdded = {
                    scope.launch {
                        onNewItemAdded(index + 1)
                    }
                },
                showReorganizeButtons = listIsFocused,
                onReorganizeUp = {
                    scope.launch {
                        onReorganizeUp(index)
                    }
                },
                onReorganizeDown = {
                    scope.launch {
                        onReorganizeDown(index)
                    }
                }
            )

            LaunchedEffect(Unit, listIsFocused, focusedIndex) {
                if (listIsFocused && focusedIndex == index) {
                    focusRequester.requestFocus()
                }
            }
        }
    }
}