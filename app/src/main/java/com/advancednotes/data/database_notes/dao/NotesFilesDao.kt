package com.advancednotes.data.database_notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.advancednotes.data.database_notes.entities.NoteFileEntity

@Dao
interface NotesFilesDao {

    /**
     * GET
     * */
    @Query("SELECT * FROM notes_files ORDER BY id DESC")
    fun getAllNotesFiles(): List<NoteFileEntity>

    @Query("SELECT * FROM notes_files WHERE note_uuid = :noteUUID ORDER BY id DESC")
    fun getAllNoteFiles(noteUUID: String): List<NoteFileEntity>

    @Query("SELECT * FROM notes_files WHERE note_uuid = :noteUUID AND directory = :directory AND deleted_permanently = 0 ORDER BY id DESC")
    fun getNoteFilesFromDirectory(noteUUID: String, directory: String): List<NoteFileEntity>

    @Query("SELECT modification_date FROM notes_files")
    fun getNotesFilesModificationDates(): List<String>

    @Query("SELECT * FROM notes_files WHERE uuid = :uuid LIMIT 1")
    fun getNoteFileByUUID(uuid: String): NoteFileEntity?

    @Query("SELECT * FROM notes_files WHERE deleted_permanently = 1 ORDER BY id DESC")
    fun getNotesFilesDeletedPermanently(): List<NoteFileEntity>

    /**
     * INSERT
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteFile(noteFileEntity: NoteFileEntity): Long

    /**
     * UPDATE
     * */
    @Update
    fun updateNoteFile(noteFileEntity: NoteFileEntity)

    @Query("UPDATE notes_files SET file_name = :newFileName, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteFileName(uuid: String, newFileName: String, modificationDate: String)

    @Query("UPDATE notes_files SET deleted_permanently = 1, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateNoteFileDeletedPermanently(uuid: String, modificationDate: String)

    /**
     * DELETE
     * */
    @Query("DELETE FROM notes_files")
    fun deleteAllNotesFiles() // CAUTION

    @Query("DELETE FROM notes_files WHERE uuid = :noteFileUUID")
    fun deleteNoteFileByUUID(noteFileUUID: String)
}