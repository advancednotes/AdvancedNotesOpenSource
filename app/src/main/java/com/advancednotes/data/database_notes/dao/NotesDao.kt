package com.advancednotes.data.database_notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyRemindersNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTagsUUIDsNoEntity
import com.advancednotes.data.database_notes.entities.NoteOnlyTrashDateNoEntity

@Dao
interface NotesDao {

    /**
     * GET
     * */
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE trash_date IS NULL AND archived = 0 AND deleted_permanently = 0 ORDER BY id DESC")
    fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE trash_date IS NULL AND archived = 1 AND deleted_permanently = 0 ORDER BY id DESC")
    fun getNotesArchived(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE trash_date IS NOT NULL AND deleted_permanently = 0 ORDER BY id DESC")
    fun getNotesTrashed(): List<NoteEntity>

    @Query("SELECT COUNT(*) FROM notes WHERE trash_date IS NULL AND archived = 0 AND deleted_permanently = 0")
    fun getCountNotes(): Int

    @Query("SELECT COUNT(*) FROM notes WHERE trash_date IS NULL AND archived = 1 AND deleted_permanently = 0")
    fun getCountArchivedNotes(): Int

    @Query("SELECT COUNT(*) FROM notes WHERE trash_date IS NOT NULL AND deleted_permanently = 0")
    fun getCountTrashedNotes(): Int

    @Query("SELECT * FROM notes WHERE uuid = :uuid LIMIT 1")
    fun getNoteByUUID(uuid: String): NoteEntity?

    @Query("SELECT uuid FROM notes WHERE id = :id LIMIT 1")
    fun getNoteUUIDById(id: Long): String

    @Query("SELECT id, uuid, tags_uuids FROM notes WHERE tags_uuids IS NOT NULL AND deleted_permanently = 0")
    fun getNotesTags(): List<NoteOnlyTagsUUIDsNoEntity>

    @Query("SELECT id, uuid, reminder FROM notes WHERE trash_date IS NULL AND reminder IS NOT NULL AND deleted_permanently = 0")
    fun getNotesReminders(): List<NoteOnlyRemindersNoEntity>

    @Query("SELECT id, uuid, trash_date FROM notes WHERE trash_date IS NOT NULL AND deleted_permanently = 0")
    fun getNotesTrashDates(): List<NoteOnlyTrashDateNoEntity>

    @Query("SELECT modification_date FROM notes")
    fun getNotesModificationDates(): List<String>

    /**
     * INSERT
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteEntity: NoteEntity): Long

    /**
     * UPDATE
     * */
    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Query("UPDATE notes SET name = :name, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteName(uuid: String, name: String, modificationDate: String)

    @Query("UPDATE notes SET advanced_content = :advancedContent, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteAdvancedContent(uuid: String, advancedContent: String?, modificationDate: String)

    @Query("UPDATE notes SET tags_uuids = :tagsUUIDs, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteTagsUUIDs(uuid: String, tagsUUIDs: String?, modificationDate: String)

    @Query("UPDATE notes SET location = :location, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteLocation(uuid: String, location: String?, modificationDate: String)

    @Query("UPDATE notes SET reminder = :reminder, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateReminder(uuid: String, reminder: String?, modificationDate: String)

    @Query("UPDATE notes SET fixed = :fixed, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteFixed(uuid: String, fixed: Boolean, modificationDate: String)

    @Query("UPDATE notes SET archived = :archived, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteArchived(uuid: String, archived: Boolean, modificationDate: String)

    @Query("UPDATE notes SET trash_date = :trashDate, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteTrashDate(uuid: String, trashDate: String?, modificationDate: String)

    @Query("UPDATE notes SET deleted_permanently = 1, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteDeletedPermanently(uuid: String, modificationDate: String)

    /**
     * DELETE
     * */
    @Query("DELETE FROM notes")
    fun deleteAllNotes() // CAUTION

    @Query("DELETE FROM notes WHERE deleted_permanently = 1")
    fun deleteNotesDeletedPermanently()
}