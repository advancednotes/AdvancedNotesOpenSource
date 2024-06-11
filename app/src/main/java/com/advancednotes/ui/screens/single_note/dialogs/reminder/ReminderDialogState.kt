package com.advancednotes.ui.screens.single_note.dialogs.reminder

import java.util.Date

data class ReminderDialogState(
    val opened: Boolean = false,
    val reminderDate: Date? = null
)