package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.api.models.NoteFileModel
import com.advancednotes.data.database_notes.entities.NoteFileEntity
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.getCurrentDateForFileName
import com.advancednotes.utils.date.DateHelper.parseDateToString
import java.util.Date

@Keep
data class NoteFile(
    val id: Long = EMPTY_ID,
    val uuid: String = EMPTY_UUID,
    val noteUUID: String = EMPTY_UUID,
    val fileUUID: String = EMPTY_UUID,
    val fileName: String = getCurrentDateForFileName(),
    val fileExtension: String = "",
    val directory: NoteDirectory = NoteDirectory.OTHER,
    val creationDate: Date = getCurrentDate(),
    val modificationDate: Date = getCurrentDate(),
    val deletedPermanently: Boolean = false
)

fun NoteFile.toApi(): NoteFileModel {
    return NoteFileModel(
        id = id,
        uuid = uuid,
        noteUUID = noteUUID,
        fileUUID = fileUUID,
        fileName = fileName,
        fileExtension = fileExtension,
        directory = directory.path,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        deletedPermanently = deletedPermanently
    )
}

fun NoteFile.toDatabase(): NoteFileEntity {
    return NoteFileEntity(
        id = id,
        uuid = uuid,
        noteUUID = noteUUID,
        fileUUID = fileUUID,
        fileName = fileName,
        fileExtension = fileExtension,
        directory = directory.path,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        deletedPermanently = deletedPermanently
    )
}