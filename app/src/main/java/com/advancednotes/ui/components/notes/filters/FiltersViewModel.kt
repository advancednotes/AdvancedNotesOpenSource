package com.advancednotes.ui.components.notes.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.Tag
import com.advancednotes.utils.notes.NoteTagsHelper.getUsedNoteTags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
) : ViewModel() {

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState: StateFlow<FiltersState> = _filtersState.asStateFlow()

    val filterChannel = Channel<Unit>(Channel.CONFLATED)

    fun getUsedTags(
        notes: List<Note>,
        tags: List<Tag>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val usedTags: List<Tag> = if (tags.isNotEmpty()) {
                getUsedNoteTags(
                    notes = notes,
                    tags = tags,
                    selectedTagsUUIDs = _filtersState.value.selectedTagsUUIDs
                )
            } else emptyList()

            val selectedTagsUUIDs: List<String> =
                _filtersState.value.selectedTagsUUIDs.filter { selectedTagUUID ->
                    usedTags.any { it.uuid == selectedTagUUID }
                }

            _filtersState.emit(
                _filtersState.value.copy(
                    usedTags = usedTags,
                    selectedTagsUUIDs = selectedTagsUUIDs
                )
            )
        }
    }

    fun setSelectedTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            val tagUUIDInList: Boolean =
                _filtersState.value.selectedTagsUUIDs.any { it == tag.uuid }

            val selectedTagsUUIDsAux: List<String> = if (tagUUIDInList) {
                _filtersState.value.selectedTagsUUIDs - tag.uuid
            } else {
                _filtersState.value.selectedTagsUUIDs + tag.uuid
            }

            _filtersState.emit(
                _filtersState.value.copy(
                    selectedTagsUUIDs = selectedTagsUUIDsAux
                )
            )

            filter()
        }
    }

    fun setSearchQuery(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _filtersState.emit(
                _filtersState.value.copy(
                    searchQuery = searchQuery
                )
            )

            if (searchQuery.isEmpty()) {
                filter()
            }
        }
    }

    fun filter() {
        viewModelScope.launch(Dispatchers.Default) {
            filterChannel.trySend(Unit)
        }
    }
}