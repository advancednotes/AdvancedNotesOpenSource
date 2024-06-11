package com.advancednotes.data.storage.repositories.impl

import android.content.Context
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.data.storage.NotesStorageDirectories.getNoteDirectory
import com.advancednotes.data.storage.NotesStorageDirectories.getNoteSpecificDirectory
import com.advancednotes.data.storage.NotesStorageDirectories.getNotesDirectory
import com.advancednotes.data.storage.repositories.NotesFilesStorageRepository
import java.io.File
import javax.inject.Inject

class NotesFilesStorageRepositoryImpl @Inject constructor(
    private val context: Context
) : NotesFilesStorageRepository {

    override fun getFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String
    ): File? {
        try {
            val directoryAux: File = getNoteSpecificDirectory(
                context = context,
                noteUUID = noteUUID,
                specificDirectory = directory
            )

            val inputFile = File(directoryAux, "$fileIdentifier.$fileExtension")

            if (inputFile.exists()) {
                return inputFile
            }
        } catch (e: Exception) {
            // e.printStackTrace()
            return null
        }

        return null
    }

    override fun saveFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String,
        tempFile: File
    ): Boolean {
        try {
            val directoryAux: File = getNoteSpecificDirectory(
                context = context,
                noteUUID = noteUUID,
                specificDirectory = directory
            )

            val outputFile = File(directoryAux, "$fileIdentifier.$fileExtension")
            tempFile.copyTo(outputFile, overwrite = true)

            return true
        } catch (e: Exception) {
            // e.printStackTrace()
            return false
        }
    }

    override fun deleteAllFiles() {
        val directory: File = getNotesDirectory(
            context = context
        )

        if (directory.exists() && directory.isDirectory) {
            directory.deleteRecursively()
        }
    }

    override fun deleteFile(
        noteUUID: String,
        directory: NoteDirectory,
        fileIdentifier: String,
        fileExtension: String
    ) {
        try {
            val directoryAux: File = getNoteSpecificDirectory(
                context = context,
                noteUUID = noteUUID,
                specificDirectory = directory
            )

            val file = File(directoryAux, "$fileIdentifier.$fileExtension")

            if (file.exists()) {
                file.deleteRecursively()

                cleanNoteSpecificDirectoryIfEmpty(
                    noteUUID = noteUUID,
                    directory = directory
                )

                cleanNoteDirectoryIfEmpty(noteUUID = noteUUID)
            }
        } catch (e: Exception) {
            // e.printStackTrace()
        }
    }

    private fun cleanNoteSpecificDirectoryIfEmpty(
        noteUUID: String,
        directory: NoteDirectory,
    ) {
        val specificDirectory: File = getNoteSpecificDirectory(
            context = context,
            noteUUID = noteUUID,
            specificDirectory = directory
        )

        val files: Array<out File>? = specificDirectory.listFiles()

        if (files != null && files.isEmpty()) {
            specificDirectory.deleteRecursively()
        }
    }

    private fun cleanNoteDirectoryIfEmpty(
        noteUUID: String
    ) {
        val noteDirectory: File = getNoteDirectory(
            context = context,
            noteUUID = noteUUID
        )

        val specificDirectories: Array<out File>? = noteDirectory.listFiles()

        if (specificDirectories != null && specificDirectories.isEmpty()) {
            noteDirectory.deleteRecursively()
        }
    }
}