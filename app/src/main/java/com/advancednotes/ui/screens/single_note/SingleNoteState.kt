package com.advancednotes.ui.screens.single_note

import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.models.Tag

data class SingleNoteState(
    val isLoading: Boolean = true,
    val gettingLocation: Boolean = false,
    val recordingAudio: Boolean = false,
    val isStickyNote: Boolean = false,
    val note: Note = Note(),
    val tags: List<Tag> = emptyList(),
    val noteImages: List<NoteFile> = emptyList(),
    val noteRecords: List<NoteFile> = emptyList(),
    val previousLocation: MyLocation? = null,
    val hasChanges: Boolean = false
)