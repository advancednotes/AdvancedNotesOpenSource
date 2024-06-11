package com.advancednotes.ui.components.single_note.advanced_content

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.AdvancedContentItemUI
import com.advancednotes.domain.models.AdvancedContentListItemUI
import com.advancednotes.domain.models.AdvancedContentTextItemUI
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.domain.models.AdvancedContentUIObject
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvancedContentViewModel @Inject constructor(
    private val gson: Gson
) : ViewModel() {

    private val _advancedContentUIState: MutableStateFlow<AdvancedContentUIState> =
        MutableStateFlow(AdvancedContentUIState())
    val advancedContentUIState: StateFlow<AdvancedContentUIState> =
        _advancedContentUIState.asStateFlow()

    val updateAdvancedContentChannel = Channel<Unit>(Channel.CONFLATED)

    fun getAdvancedContent(): List<AdvancedContentItem>? {
        return if (_advancedContentUIState.value.advancedContentUI.isNotEmpty()) {
            val advancedContent: MutableList<AdvancedContentItem> = mutableListOf()

            _advancedContentUIState.value.advancedContentUI.forEach { advancedContentItemUI ->
                when (advancedContentItemUI.type) {
                    AdvancedContentType.TEXT -> {
                        if (advancedContentItemUI.content is AdvancedContentUIObject.MyText) {
                            val contentAux =
                                advancedContentItemUI.content.value.textFieldValue.text

                            advancedContent.add(
                                AdvancedContentItem(
                                    advancedContentItemUI.type,
                                    contentAux
                                )
                            )
                        }
                    }

                    AdvancedContentType.LIST -> {
                        if (advancedContentItemUI.content is AdvancedContentUIObject.MyList) {
                            val contentAux =
                                advancedContentItemUI.content.value.map { it.textFieldValue.text }

                            advancedContent.add(
                                AdvancedContentItem(
                                    advancedContentItemUI.type,
                                    gson.toJson(contentAux)
                                )
                            )
                        }
                    }

                    AdvancedContentType.OTHER -> {}
                }
            }

            advancedContent
        } else null
    }

    fun addAllItems(items: List<AdvancedContentItem>) {
        viewModelScope.launch(Dispatchers.Default) {
            val newAdvancedContentUI: MutableList<AdvancedContentItemUI> = mutableListOf()

            items.forEach {
                when (it.type) {
                    AdvancedContentType.TEXT -> {
                        newAdvancedContentUI.add(
                            AdvancedContentItemUI(
                                type = it.type,
                                content = AdvancedContentUIObject.MyText(
                                    value = AdvancedContentTextItemUI(
                                        textFieldValue = TextFieldValue(
                                            text = it.content,
                                            selection = TextRange(it.content.length)
                                        ),
                                    )
                                )
                            )
                        )
                    }

                    AdvancedContentType.LIST -> {
                        val listAux = gson.fromJson(it.content, Array<String>::class.java).toList()

                        newAdvancedContentUI.add(
                            AdvancedContentItemUI(
                                type = it.type,
                                content = AdvancedContentUIObject.MyList(
                                    value = listAux.map { listAuxValue ->
                                        AdvancedContentListItemUI(
                                            textFieldValue = TextFieldValue(
                                                text = listAuxValue,
                                                selection = TextRange(listAuxValue.length)
                                            )
                                        )
                                    }
                                )
                            )
                        )
                    }

                    AdvancedContentType.OTHER -> {}
                }
            }

            _advancedContentUIState.value =
                _advancedContentUIState.value.copy(
                    advancedContentUI = newAdvancedContentUI
                )
        }
    }

    fun addItem(type: AdvancedContentType) {
        viewModelScope.launch(Dispatchers.Default) {
            val content: AdvancedContentUIObject = when (type) {
                AdvancedContentType.TEXT -> {
                    AdvancedContentUIObject.MyText(
                        value = AdvancedContentTextItemUI(
                            textFieldValue = TextFieldValue("")
                        )
                    )
                }

                AdvancedContentType.LIST -> {
                    val listAux = listOf("")

                    AdvancedContentUIObject.MyList(
                        value = listAux.map { listAuxValue ->
                            AdvancedContentListItemUI(
                                textFieldValue = TextFieldValue(listAuxValue)
                            )
                        }
                    )
                }

                AdvancedContentType.OTHER -> AdvancedContentUIObject.MyOther("")
            }

            val newAdvancedContentUI = _advancedContentUIState.value.advancedContentUI +
                    AdvancedContentItemUI(type, content)

            _advancedContentUIState.value =
                _advancedContentUIState.value.copy(
                    advancedContentUI = newAdvancedContentUI
                )

            setFocusedIndex(newAdvancedContentUI.lastIndex)

            updateAdvancedContent()
        }
    }

    fun removeItemByIndex(index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val newAdvancedContentUI = _advancedContentUIState.value.advancedContentUI -
                    _advancedContentUIState.value.advancedContentUI[index]

            _advancedContentUIState.value =
                _advancedContentUIState.value.copy(
                    advancedContentUI = newAdvancedContentUI
                )

            val previousIndex = index - 1
            if (_advancedContentUIState.value.focusedIndex == index) {
                setFocusedIndex(previousIndex)
            }

            updateAdvancedContent()
        }
    }

    fun onReorganizeUp(index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val previousIndex = index - 1
            val listHasPreviousIndex =
                previousIndex >= 0 && previousIndex < _advancedContentUIState.value.advancedContentUI.size

            if (listHasPreviousIndex) {
                val tempList: MutableList<AdvancedContentItemUI> =
                    _advancedContentUIState.value.advancedContentUI.toMutableList()

                val tempItem = tempList[index]

                tempList.removeAt(index)
                tempList.add(previousIndex, tempItem)

                _advancedContentUIState.value =
                    _advancedContentUIState.value.copy(
                        advancedContentUI = tempList as List<AdvancedContentItemUI>
                    )

                if (_advancedContentUIState.value.focusedIndex == index) {
                    setFocusedIndex(previousIndex)
                }

                updateAdvancedContent()
            }
        }
    }

    fun onReorganizeDown(index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val nextIndex = index + 1
            val listHasNextIndex =
                nextIndex <= _advancedContentUIState.value.advancedContentUI.lastIndex

            if (listHasNextIndex) {
                val tempList: MutableList<AdvancedContentItemUI> =
                    _advancedContentUIState.value.advancedContentUI.toMutableList()

                val tempItem = tempList[index]

                tempList.removeAt(index)
                tempList.add(nextIndex, tempItem)

                _advancedContentUIState.value =
                    _advancedContentUIState.value.copy(
                        advancedContentUI = tempList as List<AdvancedContentItemUI>
                    )

                if (_advancedContentUIState.value.focusedIndex == index) {
                    setFocusedIndex(nextIndex)
                }

                updateAdvancedContent()
            }
        }
    }

    fun setFocusedIndex(index: Int) {
        _advancedContentUIState.value =
            _advancedContentUIState.value.copy(
                focusedIndex = index
            )
    }

    fun updateAdvancedContent(index: Int, content: AdvancedContentUIObject.MyText) {
        viewModelScope.launch(Dispatchers.Default) {
            val newAdvancedContentUI =
                _advancedContentUIState.value.advancedContentUI.toMutableList()
            newAdvancedContentUI[index] = newAdvancedContentUI[index].copy(
                content = content
            )

            _advancedContentUIState.value =
                _advancedContentUIState.value.copy(
                    advancedContentUI = newAdvancedContentUI
                )

            updateAdvancedContent()
        }
    }

    fun updateAdvancedContent(index: Int, content: AdvancedContentUIObject.MyList) {
        viewModelScope.launch(Dispatchers.Default) {
            val newAdvancedContentUI =
                _advancedContentUIState.value.advancedContentUI.toMutableList()
            newAdvancedContentUI[index] = newAdvancedContentUI[index].copy(
                content = content
            )

            _advancedContentUIState.value =
                _advancedContentUIState.value.copy(
                    advancedContentUI = newAdvancedContentUI
                )

            updateAdvancedContent()
        }
    }

    private fun updateAdvancedContent() {
        viewModelScope.launch(Dispatchers.Default) {
            updateAdvancedContentChannel.trySend(Unit)
        }
    }
}