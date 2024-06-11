package com.advancednotes.ui.screens.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.NoteOnlyTagsUUIDs
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import com.advancednotes.utils.mutablelist.move
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tagsUseCase: TagsUseCase,
    private val notesUseCase: NotesUseCase
) : ViewModel() {

    private val _tagsState = MutableStateFlow(TagsState())
    val tagsState: StateFlow<TagsState> = _tagsState.asStateFlow()

    val refreshTagsChannel = Channel<Unit>(Channel.CONFLATED)

    fun getTags() {
        viewModelScope.launch(Dispatchers.IO) {
            val tagsDeferred: Deferred<List<Tag>> = async { tagsUseCase.getTags() }
            val notesOnlyTagsUUIDsDeferred: Deferred<List<NoteOnlyTagsUUIDs>> =
                async { notesUseCase.getNotesTags() }

            val tags: List<Tag> = tagsDeferred.await()
            val notesOnlyTagsUUIDs: List<NoteOnlyTagsUUIDs> = notesOnlyTagsUUIDsDeferred.await()

            val tagsWithNotesCount: MutableList<Pair<Tag, Int>> = mutableListOf()

            tags.forEach { tag ->
                val notesCount: Int = notesOnlyTagsUUIDs.sumOf {
                    it.tagsUUIDs.count { uuid -> uuid == tag.uuid }
                }

                tagsWithNotesCount.add(Pair(tag, notesCount))
            }

            _tagsState.value = _tagsState.value.copy(
                isLoading = false,
                tagsWithNotesCount = tagsWithNotesCount
            )
        }
    }

    fun refreshTags() {
        viewModelScope.launch(Dispatchers.Default) {
            refreshTagsChannel.trySend(Unit)
        }
    }

    fun onReorganize(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val newTagsWithNotesCount: MutableList<Pair<Tag, Int>> =
                _tagsState.value.tagsWithNotesCount.toMutableList()

            newTagsWithNotesCount.move(fromIndex, toIndex)

            updateTagsOrders(newTagsWithNotesCount)
        }
    }

    private suspend fun updateTagsOrders(newTagsWithNotesCount: MutableList<Pair<Tag, Int>>) {
        newTagsWithNotesCount.forEachIndexed { index, pair ->
            val (tag: Tag, notesCount: Int) = pair

            if (tag.orderNumber != index) {
                withContext(Dispatchers.IO) {
                    tagsUseCase.updateTagOrderNumber(
                        uuid = tag.uuid,
                        orderNumber = index,
                    )
                }
            }
        }

        _tagsState.value = _tagsState.value.copy(
            tagsWithNotesCount = newTagsWithNotesCount
        )
    }
}