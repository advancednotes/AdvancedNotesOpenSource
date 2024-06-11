package com.advancednotes.data.database_notes.repositories.impl

import com.advancednotes.data.database_notes.dao.NotesFilesDao
import com.advancednotes.data.database_notes.entities.NoteFileEntity
import com.advancednotes.data.database_notes.repositories.NotesFilesRepository
import javax.inject.Inject

class NotesFilesRepositoryImpl @Inject constructor(
    private val notesFilesDao: NotesFilesDao
) : NotesFilesRepository {

    override fun getAllNotesFiles(): List<NoteFileEntity> {
        return notesFilesDao.getAllNotesFiles()
    }

    override fun getAllNoteFiles(noteUUID: String): List<NoteFileEntity> {
        return notesFilesDao.getAllNoteFiles(noteUUID)
    }

    override fun getNoteFilesFromDirectory(
        noteUUID: String,
        directory: String
    ): List<NoteFileEntity> {
        return notesFilesDao.getNoteFilesFromDirectory(noteUUID, directory)
    }

    override fun getNotesFilesModificationDates(): List<String> {
        return notesFilesDao.getNotesFilesModificationDates()
    }

    override fun getNoteFileByUUID(uuid: String): NoteFileEntity? {
        return notesFilesDao.getNoteFileByUUID(uuid)
    }

    override fun getNotesFilesDeletedPermanently(): List<NoteFileEntity> {
        return notesFilesDao.getNotesFilesDeletedPermanently()
    }

    override fun insertNoteFile(noteFileEntity: NoteFileEntity): Long {
        return notesFilesDao.insertNoteFile(noteFileEntity)
    }

    override fun updateNoteFile(noteFileEntity: NoteFileEntity) {
        notesFilesDao.updateNoteFile(noteFileEntity)
    }

    override fun updateNoteFileName(
        uuid: String,
        newFileName: String,
        modificationDate: String
    ) {
        notesFilesDao.updateNoteFileName(
            uuid = uuid,
            newFileName = newFileName,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteFileDeletedPermanently(uuid: String, modificationDate: String) {
        notesFilesDao.updateNoteFileDeletedPermanently(uuid, modificationDate)
    }

    override fun deleteAllNotesFiles() {
        notesFilesDao.deleteAllNotesFiles()
    }

    override fun deleteNoteFileByUUID(uuid: String) {
        notesFilesDao.deleteNoteFileByUUID(uuid)
    }
}