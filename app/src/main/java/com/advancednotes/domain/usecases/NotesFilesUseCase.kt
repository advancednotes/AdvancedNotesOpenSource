package com.advancednotes.domain.usecases

import android.content.Context
import android.net.Uri
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.domain.models.NoteFile
import java.io.File
import java.util.Date

interface NotesFilesUseCase {

    fun duplicateNoteFiles(
        oldNoteUUID: String,
        newNoteUUID: String
    )

    fun getAllNotesFiles(): List<NoteFile>
    fun getAllNoteFiles(noteUUID: String): List<NoteFile>
    fun getNoteFilesFromDirectory(noteUUID: String, directory: NoteDirectory): List<NoteFile>
    fun getNotesFilesModificationDates(): List<Date>
    fun getNoteFileByUUID(uuid: String): NoteFile
    fun getNotesFilesDeletedPermanently(): List<NoteFile>

    fun getFile(noteFile: NoteFile): File?
    fun getFileUriFromNoteFile(context: Context, noteFile: NoteFile): Uri?

    fun insertNoteFile(noteFile: NoteFile, tempFile: File): Long

    fun updateNoteFile(noteFile: NoteFile, tempFile: File)
    fun updateNoteFileName(
        uuid: String,
        newFileName: String
    )

    fun updateAllNoteFilesByNoteUUIDDeletedPermanently(noteUUID: String)
    fun updateNoteFileDeletedPermanently(uuid: String)

    fun deleteAllNotesFiles()
    fun deleteNotesFilesDeletedPermanently()
    fun deleteNoteFile(noteFile: NoteFile)
}