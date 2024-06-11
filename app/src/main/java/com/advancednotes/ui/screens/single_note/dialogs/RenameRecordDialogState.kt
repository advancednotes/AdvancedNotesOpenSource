package com.advancednotes.ui.screens.single_note.dialogs

import com.advancednotes.domain.models.NoteFile

data class RenameRecordDialogState(
    val opened: Boolean = false,
    val noteRecord: NoteFile? = null
)