package com.advancednotes.data.storage.repositories

import com.advancednotes.data.storage.NoteDirectory
import java.io.File

interface NotesFilesStorageRepository {

    fun getFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String
    ): File?

    fun saveFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String,
        tempFile: File
    ): Boolean

    fun deleteAllFiles()

    fun deleteFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String
    )
}