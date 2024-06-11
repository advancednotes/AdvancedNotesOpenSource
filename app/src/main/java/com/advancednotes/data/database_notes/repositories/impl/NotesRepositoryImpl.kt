package com.advancednotes.data.database_notes.repositories.impl

import com.advancednotes.data.database_notes.dao.NotesDao
import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyRemindersNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTagsUUIDsNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTrashDateNoEntity
import com.advancednotes.data.database_notes.repositories.NotesRepository
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override fun getAllNotes(): List<NoteEntity> {
        return notesDao.getAllNotes()
    }

    override fun getNotes(): List<NoteEntity> {
        return notesDao.getNotes()
    }

    override fun getNotesArchived(): List<NoteEntity> {
        return notesDao.getNotesArchived()
    }

    override fun getNotesTrashed(): List<NoteEntity> {
        return notesDao.getNotesTrashed()
    }

    override fun getCountNotes(): Int {
        return notesDao.getCountNotes()
    }

    override fun getCountArchivedNotes(): Int {
        return notesDao.getCountArchivedNotes()
    }

    override fun getCountTrashedNotes(): Int {
        return notesDao.getCountTrashedNotes()
    }

    override fun getNoteByUUID(uuid: String): NoteEntity? {
        return notesDao.getNoteByUUID(
            uuid = uuid
        )
    }

    override fun getNoteUUIDById(id: Long): String {
        return notesDao.getNoteUUIDById(
            id = id
        )
    }

    override fun getNotesTags(): List<NoteOnlyTagsUUIDsNoEntity> {
        return notesDao.getNotesTags()
    }

    override fun getNotesReminders(): List<NoteOnlyRemindersNoEntity> {
        return notesDao.getNotesReminders()
    }

    override fun getNotesTrashDates(): List<NoteOnlyTrashDateNoEntity> {
        return notesDao.getNotesTrashDates()
    }

    override fun getNotesModificationDates(): List<String> {
        return notesDao.getNotesModificationDates()
    }

    override fun insertNote(noteEntity: NoteEntity): Long {
        return notesDao.insertNote(
            noteEntity = noteEntity
        )
    }

    override fun updateNote(noteEntity: NoteEntity) {
        notesDao.updateNote(
            noteEntity = noteEntity
        )
    }

    override fun updateNoteName(uuid: String, name: String, modificationDate: String) {
        notesDao.updateNoteName(
            uuid = uuid,
            name = name,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteContent(
        uuid: String,
        advancedContent: String?,
        modificationDate: String
    ) {
        notesDao.updateNoteAdvancedContent(
            uuid = uuid,
            advancedContent = advancedContent,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteTagsUUIDs(uuid: String, tagsUUIDs: String?, modificationDate: String) {
        notesDao.updateNoteTagsUUIDs(
            uuid = uuid,
            tagsUUIDs = tagsUUIDs,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteLocation(uuid: String, location: String?, modificationDate: String) {
        notesDao.updateNoteLocation(
            uuid = uuid,
            location = location,
            modificationDate = modificationDate
        )
    }

    override fun updateReminder(uuid: String, reminder: String?, modificationDate: String) {
        notesDao.updateReminder(
            uuid = uuid,
            reminder = reminder,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteFixed(uuid: String, fixed: Boolean, modificationDate: String) {
        notesDao.updateNoteFixed(
            uuid = uuid,
            fixed = fixed,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteArchived(uuid: String, archived: Boolean, modificationDate: String) {
        notesDao.updateNoteArchived(
            uuid = uuid,
            archived = archived,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteTrashDate(uuid: String, trashDate: String?, modificationDate: String) {
        notesDao.updateNoteTrashDate(
            uuid = uuid,
            trashDate = trashDate,
            modificationDate = modificationDate
        )
    }

    override fun updateNoteDeletedPermanently(uuid: String, modificationDate: String) {
        notesDao.updateNoteDeletedPermanently(uuid, modificationDate)
    }

    override fun deleteAllNotes() {
        notesDao.deleteAllNotes()
    }

    override fun deleteNotesDeletedPermanently() {
        notesDao.deleteNotesDeletedPermanently()
    }
}