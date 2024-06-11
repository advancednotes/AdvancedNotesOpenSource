package com.advancednotes.ui.screens.single_tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteOnlyTagsUUIDs
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.models.isEmpty
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.TagsUseCase
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
class SingleTagViewModel @Inject constructor(
    private val tagsUseCase: TagsUseCase,
    private val notesUseCase: NotesUseCase
) : ViewModel() {

    private val _singleTagState = MutableStateFlow(SingleTagState())
    val singleTagState: StateFlow<SingleTagState> = _singleTagState.asStateFlow()

    val snackbarDiscardChannel = Channel<Unit>(Channel.CONFLATED)

    val exitChannel = Channel<Unit>(Channel.CONFLATED)

    fun getTag(tagUUID: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val tagDeferred: Deferred<Tag> =
                async(Dispatchers.IO) { tagsUseCase.getTagByUUID(tagUUID) }

            val notesOnlyTagsUUIDsDeferred: Deferred<List<NoteOnlyTagsUUIDs>> =
                async(Dispatchers.IO) { notesUseCase.getNotesTags() }

            val notesDeferred: Deferred<List<Note>> =
                async(Dispatchers.IO) { notesUseCase.getNotes(filterNoteTagsUUIDs = listOf(tagUUID)) }

            val notesArchivedDeferred: Deferred<List<Note>> =
                async(Dispatchers.IO) {
                    notesUseCase.getNotesArchived(
                        filterNoteTagsUUIDs = listOf(
                            tagUUID
                        )
                    )
                }

            val notesTrashedDeferred: Deferred<List<Note>> =
                async(Dispatchers.IO) {
                    notesUseCase.getNotesTrashed(
                        filterNoteTagsUUIDs = listOf(
                            tagUUID
                        )
                    )
                }

            val tag: Tag = tagDeferred.await()

            val notesOnlyTagsUUIDs: List<NoteOnlyTagsUUIDs> = notesOnlyTagsUUIDsDeferred.await()
            val notesInTag: List<Note> = notesDeferred.await()
            val notesArchivedInTag: List<Note> = notesArchivedDeferred.await()
            val notesTrashedInTag: List<Note> = notesTrashedDeferred.await()

            val notesCount: Int = notesOnlyTagsUUIDs.sumOf {
                it.tagsUUIDs.count { uuid -> uuid == tag.uuid }
            }

            withContext(Dispatchers.Main) {
                _singleTagState.value = _singleTagState.value.copy(
                    isLoading = false,
                    tag = tag,
                    notesInTag = notesInTag,
                    notesArchivedInTag = notesArchivedInTag,
                    notesTrashedInTag = notesTrashedInTag,
                    notesCount = notesCount
                )
            }
        }
    }

    fun changeTagName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _singleTagState.value = _singleTagState.value.copy(
                tag = _singleTagState.value.tag.copy(
                    name = name
                ),
                hasChanges = true
            )
        }
    }

    fun changeTagColor(colorHex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _singleTagState.value = _singleTagState.value.copy(
                tag = _singleTagState.value.tag.copy(
                    colorHex = colorHex
                ),
                hasChanges = true
            )
        }
    }

    fun deleteTagPermanently(tagUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagsUseCase.updateTagDeletedPermanently(uuid = tagUUID)

            exit()
        }
    }

    fun showSnackbarDiscardChanges() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarDiscardChannel.trySend(Unit)
        }
    }

    fun isNewTag(): Boolean = _singleTagState.value.tag.uuid == EMPTY_UUID

    fun isNewTagWithEmptyData(): Boolean {
        if (!isNewTag()) {
            return false
        }

        if (_singleTagState.value.tag.isEmpty()) {
            return true
        }

        return false
    }

    fun validateData(): Boolean {
        return _singleTagState.value.tag.name.isNotEmpty()
    }

    fun saveTagAndExit() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNewTag()) {
                tagsUseCase.insertTag(
                    tag = _singleTagState.value.tag
                )
            } else {
                tagsUseCase.updateTag(
                    tag = _singleTagState.value.tag
                )
            }

            exit()
        }
    }

    fun exit() {
        viewModelScope.launch(Dispatchers.Default) {
            exitChannel.trySend(Unit)
        }
    }
}