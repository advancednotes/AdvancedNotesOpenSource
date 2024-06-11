package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.data.storage.getNoteDirectoryByPath
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.utils.date.DateHelper.parseStringToDate

@Entity(tableName = "notes_files")
data class NoteFileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "note_uuid")
    val noteUUID: String,

    @ColumnInfo(name = "file_uuid")
    val fileUUID: String,

    @ColumnInfo(name = "file_name")
    val fileName: String,

    @ColumnInfo(name = "file_extension")
    val fileExtension: String,

    @ColumnInfo(name = "directory")
    val directory: String,

    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "modification_date")
    val modificationDate: String,

    @ColumnInfo(name = "deleted_permanently")
    val deletedPermanently: Boolean
)

fun NoteFileEntity.toDomain(): NoteFile {
    return NoteFile(
        id = id,
        uuid = uuid,
        noteUUID = noteUUID,
        fileUUID = fileUUID,
        fileName = fileName,
        fileExtension = fileExtension,
        directory = getNoteDirectoryByPath(directory) ?: NoteDirectory.OTHER,
        creationDate = parseStringToDate(creationDate),
        modificationDate = parseStringToDate(modificationDate),
        deletedPermanently = deletedPermanently
    )
}