package com.advancednotes.di

import android.content.Context
import com.advancednotes.data.storage.repositories.NotesFilesStorageRepository
import com.advancednotes.data.storage.repositories.impl.NotesFilesStorageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageRepositoryModule {

    @Provides
    @Singleton
    fun provideNotesFilesStorageRepository(
        @ApplicationContext context: Context
    ): NotesFilesStorageRepository {
        return NotesFilesStorageRepositoryImpl(context)
    }
}