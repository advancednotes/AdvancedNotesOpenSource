package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import com.advancednotes.domain.models.NoteOnlyReminders
import com.advancednotes.domain.models.parseNoteReminderToDomain

data class NoteOnlyRemindersNoEntity(
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "reminder")
    val reminder: String?
)

fun NoteOnlyRemindersNoEntity.toDomain(): NoteOnlyReminders {
    return NoteOnlyReminders(
        id = id,
        uuid = uuid,
        reminder = parseNoteReminderToDomain(reminder)
    )
}