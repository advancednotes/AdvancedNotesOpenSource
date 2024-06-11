package com.advancednotes.ui.screens.notes.dialogs

import com.advancednotes.common.Constants.Companion.EMPTY_UUID

data class NoteDialogState(
    val opened: Boolean = false,
    val noteUUID: String = EMPTY_UUID
)