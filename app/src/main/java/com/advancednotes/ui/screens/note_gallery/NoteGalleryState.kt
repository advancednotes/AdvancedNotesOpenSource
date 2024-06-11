package com.advancednotes.ui.screens.note_gallery

import com.advancednotes.domain.models.NoteFile

data class NoteGalleryState(
    val noteFile: NoteFile = NoteFile()
)