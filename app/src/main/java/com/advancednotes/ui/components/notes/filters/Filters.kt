package com.advancednotes.ui.components.notes.filters

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.ui.components.buttons.MyIconButtonSmall
import com.advancednotes.ui.components.textfields.AdvancedTextField
import com.advancednotes.ui.screens.notes.NotesState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun Filters(
    notesState: NotesState,
    onFilter: (searchQuery: String, usedTagsUUIDs: List<String>) -> Unit,
    filtersViewModel: FiltersViewModel,
    modifier: Modifier = Modifier
) {
    val filtersState: FiltersState by filtersViewModel.filtersState.collectAsState()

    var searchQuery: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    var tagsDialogOpened: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(notesState.notes, notesState.tags) {
        filtersViewModel.getUsedTags(
            notesState.notes,
            notesState.tags
        )
    }

    LaunchedEffect(filtersViewModel.filterChannel) {
        filtersViewModel.filterChannel.receiveAsFlow().collectLatest {
            onFilter(filtersState.searchQuery, filtersState.selectedTagsUUIDs)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AdvancedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                filtersViewModel.setSearchQuery(newValue.text)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleMedium,
            placeholder = stringResource(id = R.string.search_hint),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (filtersState.usedTags.size >= 5) {
                        MyIconButtonSmall(
                            icon = Icons.Default.Tag,
                            onClick = {
                                tagsDialogOpened = true
                            },
                            contentDescription = stringResource(id = R.string.screen_tags_label)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { filtersViewModel.filter() }
            ),
            singleLine = true,
            borderWidth = 1.5.dp
        )

        if (filtersState.usedTags.isNotEmpty()) {
            FiltersChipGroup(
                usedTags = filtersState.usedTags,
                selectedTagsUUIDs = filtersState.selectedTagsUUIDs,
                onClick = { tag ->
                    filtersViewModel.setSelectedTag(tag)
                }
            )
        }
    }

    FilterTagsDialog(
        opened = tagsDialogOpened,
        onCloseDialog = {
            tagsDialogOpened = false
        },
        usedTags = filtersState.usedTags,
        selectedTagsUUIDs = filtersState.selectedTagsUUIDs,
        onClickTag = { tag ->
            filtersViewModel.setSelectedTag(tag)
        }
    )
}