package com.advancednotes.data.database_notes.repositories

import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyRemindersNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTagsUUIDsNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTrashDateNoEntity

interface NotesRepository {

    fun getAllNotes(): List<NoteEntity>
    fun getNotes(): List<NoteEntity>
    fun getNotesArchived(): List<NoteEntity>
    fun getNotesTrashed(): List<NoteEntity>
    fun getCountNotes(): Int
    fun getCountArchivedNotes(): Int
    fun getCountTrashedNotes(): Int
    fun getNoteByUUID(uuid: String): NoteEntity?
    fun getNoteUUIDById(id: Long): String
    fun getNotesTags(): List<NoteOnlyTagsUUIDsNoEntity>
    fun getNotesReminders(): List<NoteOnlyRemindersNoEntity>
    fun getNotesTrashDates(): List<NoteOnlyTrashDateNoEntity>
    fun getNotesModificationDates(): List<String>
    fun insertNote(noteEntity: NoteEntity): Long
    fun updateNote(noteEntity: NoteEntity)
    fun updateNoteName(uuid: String, name: String, modificationDate: String)
    fun updateNoteContent(uuid: String, advancedContent: String?, modificationDate: String)
    fun updateNoteTagsUUIDs(uuid: String, tagsUUIDs: String?, modificationDate: String)
    fun updateNoteLocation(uuid: String, location: String?, modificationDate: String)
    fun updateReminder(uuid: String, reminder: String?, modificationDate: String)
    fun updateNoteFixed(uuid: String, fixed: Boolean, modificationDate: String)
    fun updateNoteArchived(uuid: String, archived: Boolean, modificationDate: String)
    fun updateNoteTrashDate(uuid: String, trashDate: String?, modificationDate: String)
    fun updateNoteDeletedPermanently(uuid: String, modificationDate: String)
    fun deleteAllNotes()
    fun deleteNotesDeletedPermanently()
}