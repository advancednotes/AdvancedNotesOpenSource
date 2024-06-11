package com.advancednotes.ui.components.single_note.advanced_content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.domain.models.AdvancedContentUIObject
import com.advancednotes.ui.components.buttons.ReorganizeButtons
import com.advancednotes.ui.components.cards.AdvancedStyledCard
import com.advancednotes.ui.components.cards.AdvancedStyledCardIntensityLevel
import com.advancednotes.ui.components.single_note.advanced_content.list.AdvancedContentList
import com.advancednotes.ui.components.single_note.advanced_content.textfield.AdvancedContentText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun AdvancedContent(
    isLoading: Boolean,
    advancedContent: List<AdvancedContentItem>?,
    onUpdateAdvancedContent: ((advancedContent: List<AdvancedContentItem>?) -> Unit)? = null,
    advancedContentViewModel: AdvancedContentViewModel = hiltViewModel()
) {
    val advancedContentUIState: AdvancedContentUIState by advancedContentViewModel.advancedContentUIState.collectAsState()

    LaunchedEffect(isLoading) {
        advancedContent?.let { advancedContentItems ->
            advancedContentViewModel.addAllItems(advancedContentItems)
        }
    }

    LaunchedEffect(advancedContentViewModel.updateAdvancedContentChannel) {
        advancedContentViewModel.updateAdvancedContentChannel.receiveAsFlow().collectLatest {
            onUpdateAdvancedContent?.invoke(advancedContentViewModel.getAdvancedContent())
        }
    }

    AdvancedStyledCard(
        intensityLevel = AdvancedStyledCardIntensityLevel.LOW
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (advancedContentUIState.advancedContentUI.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    advancedContentUIState.advancedContentUI.forEachIndexed { index, item ->
                        when (item.type) {
                            AdvancedContentType.TEXT -> {
                                if (item.content is AdvancedContentUIObject.MyText) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (advancedContentUIState.focusedIndex != -1) {
                                            ReorganizeButtons(
                                                onReorganizeUp = {
                                                    advancedContentViewModel.onReorganizeUp(index)
                                                },
                                                onReorganizeDown = {
                                                    advancedContentViewModel.onReorganizeDown(index)
                                                }
                                            )
                                        }

                                        val focusRequester = remember { FocusRequester() }

                                        AdvancedContentText(
                                            content = item.content,
                                            onContentChange = { content ->
                                                advancedContentViewModel.updateAdvancedContent(
                                                    index,
                                                    content
                                                )
                                            },
                                            focusRequester = focusRequester,
                                            onFocused = {
                                                advancedContentViewModel.setFocusedIndex(index)
                                            },
                                            onTextFieldRemoved = {
                                                advancedContentViewModel.removeItemByIndex(index)
                                            }
                                        )

                                        LaunchedEffect(Unit, advancedContentUIState.focusedIndex) {
                                            if (advancedContentUIState.focusedIndex == index) {
                                                focusRequester.requestFocus()
                                            }
                                        }
                                    }
                                }
                            }

                            AdvancedContentType.LIST -> {
                                if (item.content is AdvancedContentUIObject.MyList) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (advancedContentUIState.focusedIndex != -1) {
                                            ReorganizeButtons(
                                                onReorganizeUp = {
                                                    advancedContentViewModel.onReorganizeUp(index)
                                                },
                                                onReorganizeDown = {
                                                    advancedContentViewModel.onReorganizeDown(index)
                                                }
                                            )
                                        }

                                        AdvancedContentList(
                                            content = item.content,
                                            onContentChange = { content ->
                                                advancedContentViewModel.updateAdvancedContent(
                                                    index,
                                                    content
                                                )
                                            },
                                            onListRemoved = {
                                                advancedContentViewModel.removeItemByIndex(index)
                                            },
                                            onSomeItemFocused = {
                                                advancedContentViewModel.setFocusedIndex(index)
                                            },
                                            listIsFocused = advancedContentUIState.focusedIndex == index
                                        )
                                    }
                                }
                            }

                            AdvancedContentType.OTHER -> {

                            }
                        }
                    }
                }
            }

            val addButtonsOrientation: AdvancedContentAddButtonsOrientation =
                if (advancedContentUIState.advancedContentUI.isEmpty()) {
                    AdvancedContentAddButtonsOrientation.VERTICAL
                } else AdvancedContentAddButtonsOrientation.HORIZONTAL

            AdvancedContentAddButtons(
                onAddText = {
                    advancedContentViewModel.addItem(AdvancedContentType.TEXT)
                },
                onAddList = {
                    advancedContentViewModel.addItem(AdvancedContentType.LIST)
                },
                orientation = addButtonsOrientation,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}