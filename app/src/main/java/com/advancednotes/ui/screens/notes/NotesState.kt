package com.advancednotes.ui.screens.notes

import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.Tag

data class NotesState(
    val isLoading: Boolean = true,
    val notes: List<Note> = emptyList(),
    val tags: List<Tag> = emptyList()
)