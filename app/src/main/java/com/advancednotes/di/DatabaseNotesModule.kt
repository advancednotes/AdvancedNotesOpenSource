package com.advancednotes.di

import android.content.Context
import androidx.room.Room
import com.advancednotes.data.database_notes.DatabaseNotes
import com.advancednotes.data.database_notes.dao.NotesDao
import com.advancednotes.data.database_notes.dao.NotesFilesDao
import com.advancednotes.data.database_notes.dao.TagsDao
import com.advancednotes.data.database_notes.migrations.MIGRATION_1_2
import com.advancednotes.data.database_notes.migrations.MIGRATION_2_3
import com.advancednotes.data.database_notes.migrations.MIGRATION_3_4
import com.advancednotes.data.database_notes.migrations.MIGRATION_4_5
import com.advancednotes.data.database_notes.migrations.MIGRATION_5_6
import com.advancednotes.data.database_notes.migrations.MIGRATION_6_7
import com.advancednotes.data.database_notes.migrations.MIGRATION_7_8
import com.advancednotes.di.qualifiers.DatabaseQualifiers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseNotesModule {

    @Provides
    fun provideNotesDao(
        @DatabaseQualifiers.DatabaseNotes databaseNotes: DatabaseNotes
    ): NotesDao {
        return databaseNotes.notesDao()
    }

    @Provides
    fun provideNotesFilesDao(
        @DatabaseQualifiers.DatabaseNotes databaseNotes: DatabaseNotes
    ): NotesFilesDao {
        return databaseNotes.notesFilesDao()
    }

    @Provides
    fun provideTagsDao(
        @DatabaseQualifiers.DatabaseNotes databaseNotes: DatabaseNotes
    ): TagsDao {
        return databaseNotes.tagsDao()
    }

    @Provides
    @Singleton
    @DatabaseQualifiers.DatabaseNotes
    fun provideDatabaseNotes(
        @ApplicationContext context: Context
    ): DatabaseNotes {
        return Room.databaseBuilder(context, DatabaseNotes::class.java, "advancednotes.db")
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8
            ).build()
    }
}