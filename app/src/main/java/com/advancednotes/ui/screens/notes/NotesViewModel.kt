package com.advancednotes.ui.screens.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.R
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NotesQuery
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.models.getUncategorized
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCase: NotesUseCase,
    private val tagsUseCase: TagsUseCase
) : ViewModel() {

    private val _notesState = MutableStateFlow(NotesState())
    val notesState: StateFlow<NotesState> = _notesState.asStateFlow()

    val refreshNotesChannel = Channel<Unit>(Channel.CONFLATED)

    fun getNotes(
        notesQuery: NotesQuery,
        uncategorizedTagName: String,
        filterQuery: String = "",
        filterNoteTagsUUIDs: List<String> = emptyList()
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes: List<Note> = when (notesQuery) {
                NotesQuery.ALL -> {
                    notesUseCase.getNotes(
                        filterQuery = filterQuery,
                        filterNoteTagsUUIDs = filterNoteTagsUUIDs
                    )
                }

                NotesQuery.ARCHIVED -> notesUseCase.getNotesArchived(
                    filterQuery = filterQuery,
                    filterNoteTagsUUIDs = filterNoteTagsUUIDs
                )

                NotesQuery.TRASHED -> notesUseCase.getNotesTrashed(
                    filterQuery = filterQuery,
                    filterNoteTagsUUIDs = filterNoteTagsUUIDs
                )
            }

            val tags: List<Tag> = tagsUseCase.getTags()
            val uncategorizedTag = Tag().getUncategorized(uncategorizedTagName)

            _notesState.value = _notesState.value.copy(
                isLoading = false,
                notes = notes,
                tags = tags + uncategorizedTag
            )
        }
    }

    fun fixNote(noteUUID: String, fixed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteFixed(noteUUID, !fixed)

            refreshNotes()
        }
    }

    fun duplicateNote(context: Context, noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val note: Note = notesUseCase.getNoteByUUID(noteUUID)
            val newNote: Note = note.copy(
                name = "${note.name} - ${context.getString(R.string.duplicate_sufix)}"
            )

            notesUseCase.duplicateNote(context, newNote)

            refreshNotes()
        }
    }

    fun archiveNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteArchived(
                uuid = noteUUID,
                archived = true
            )

            refreshNotes()
        }
    }

    fun unarchiveNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteArchived(
                uuid = noteUUID,
                archived = false
            )

            refreshNotes()
        }
    }

    fun restoreNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteTrash(
                uuid = noteUUID,
                trash = false
            )

            refreshNotes()
        }
    }

    fun trashNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteTrash(
                uuid = noteUUID,
                trash = true
            )

            refreshNotes()
        }
    }

    fun deleteNotePermanently(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteDeletedPermanently(noteUUID)

            refreshNotes()
        }
    }

    fun deleteNotesTrashed() {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.deleteNotesTrashed()

            refreshNotes()
        }
    }

    private fun refreshNotes() {
        viewModelScope.launch(Dispatchers.Default) {
            refreshNotesChannel.trySend(Unit)
        }
    }
}