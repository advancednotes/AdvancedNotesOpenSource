package com.advancednotes.ui.screens.single_note.dialogs

import com.advancednotes.domain.models.NoteFile

data class DeleteRecordDialogState(
    val opened: Boolean = false,
    val noteRecord: NoteFile = NoteFile()
)