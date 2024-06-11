package com.advancednotes.data.database_notes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.advancednotes.data.database_notes.dao.NotesDao
import com.advancednotes.data.database_notes.dao.NotesFilesDao
import com.advancednotes.data.database_notes.dao.TagsDao
import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.data.database_notes.entities.NoteFileEntity
import com.advancednotes.data.database_notes.entities.TagEntity

@Database(
    entities = [NoteEntity::class, NoteFileEntity::class, TagEntity::class],
    version = 8
)

abstract class DatabaseNotes : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun notesFilesDao(): NotesFilesDao
    abstract fun tagsDao(): TagsDao
}