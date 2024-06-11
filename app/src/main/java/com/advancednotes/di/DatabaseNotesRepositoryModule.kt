package com.advancednotes.di

import com.advancednotes.data.database_notes.dao.NotesDao
import com.advancednotes.data.database_notes.dao.NotesFilesDao
import com.advancednotes.data.database_notes.dao.TagsDao
import com.advancednotes.data.database_notes.repositories.NotesFilesRepository
import com.advancednotes.data.database_notes.repositories.NotesRepository
import com.advancednotes.data.database_notes.repositories.TagsRepository
import com.advancednotes.data.database_notes.repositories.impl.NotesFilesRepositoryImpl
import com.advancednotes.data.database_notes.repositories.impl.NotesRepositoryImpl
import com.advancednotes.data.database_notes.repositories.impl.TagsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseNotesRepositoryModule {

    @Provides
    @Singleton
    fun provideNotesRepositoryImpl(
        notesDao: NotesDao
    ): NotesRepository {
        return NotesRepositoryImpl(notesDao)
    }

    @Provides
    @Singleton
    fun provideNotesFilesRepositoryImpl(
        notesFilesDao: NotesFilesDao
    ): NotesFilesRepository {
        return NotesFilesRepositoryImpl(notesFilesDao)
    }

    @Provides
    @Singleton
    fun provideNoteTagsRepositoryImpl(
        tagsDao: TagsDao
    ): TagsRepository {
        return TagsRepositoryImpl(tagsDao)
    }
}