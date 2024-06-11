package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.data.storage.getNoteDirectoryByPath
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.google.gson.annotations.SerializedName

@Keep
data class NoteFileModel(
    @SerializedName("id") val id: Long,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("noteUUID") val noteUUID: String,
    @SerializedName("fileUUID") val fileUUID: String,
    @SerializedName("fileName") val fileName: String,
    @SerializedName("fileExtension") val fileExtension: String,
    @SerializedName("directory") val directory: String,
    @SerializedName("creationDate") val creationDate: String,
    @SerializedName("modificationDate") val modificationDate: String,
    @SerializedName("deletedPermanently") val deletedPermanently: Boolean
)

fun NoteFileModel.toDomain(): NoteFile {
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