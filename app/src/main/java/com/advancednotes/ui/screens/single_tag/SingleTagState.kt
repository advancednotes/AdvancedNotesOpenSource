package com.advancednotes.ui.screens.single_tag

import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.Tag
import com.advancednotes.utils.notes.NoteTagsHelper.getNoteTagsDefaultColors

data class SingleTagState(
    val isLoading: Boolean = true,
    val tag: Tag = Tag(),
    val notesInTag: List<Note> = emptyList(),
    val notesArchivedInTag: List<Note> = emptyList(),
    val notesTrashedInTag: List<Note> = emptyList(),
    val notesCount: Int = 1,
    val colors: List<String> = getNoteTagsDefaultColors(),
    val hasChanges: Boolean = false
)