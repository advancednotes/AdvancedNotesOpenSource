package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import com.advancednotes.domain.models.NoteOnlyTrashDate
import com.advancednotes.utils.date.DateHelper

data class NoteOnlyTrashDateNoEntity(
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "trash_date")
    val trashDate: String?,
)

fun NoteOnlyTrashDateNoEntity.toDomain(): NoteOnlyTrashDate {
    return NoteOnlyTrashDate(
        id = id,
        uuid = uuid,
        trashDate = trashDate?.let { DateHelper.parseStringToDate(it) },
    )
}