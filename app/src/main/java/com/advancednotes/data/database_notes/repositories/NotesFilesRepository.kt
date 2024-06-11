package com.advancednotes.data.database_notes.repositories

import com.advancednotes.data.database_notes.entities.NoteFileEntity

interface NotesFilesRepository {

    fun getAllNotesFiles(): List<NoteFileEntity>
    fun getAllNoteFiles(noteUUID: String): List<NoteFileEntity>
    fun getNoteFilesFromDirectory(noteUUID: String, directory: String): List<NoteFileEntity>
    fun getNotesFilesModificationDates(): List<String>
    fun getNoteFileByUUID(uuid: String): NoteFileEntity?
    fun getNotesFilesDeletedPermanently(): List<NoteFileEntity>
    fun insertNoteFile(noteFileEntity: NoteFileEntity): Long
    fun updateNoteFile(noteFileEntity: NoteFileEntity)
    fun updateNoteFileName(
        uuid: String,
        newFileName: String,
        modificationDate: String
    )

    fun updateNoteFileDeletedPermanently(uuid: String, modificationDate: String)
    fun deleteAllNotesFiles()
    fun deleteNoteFileByUUID(uuid: String)
}