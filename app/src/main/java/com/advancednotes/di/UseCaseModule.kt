package com.advancednotes.di

import android.content.Context
import com.advancednotes.data.api.repositories.ApiAuthenticationRepository
import com.advancednotes.data.api.repositories.ApiDownloaderRepository
import com.advancednotes.data.api.repositories.ApiPreSynchronizerRepository
import com.advancednotes.data.api.repositories.ApiSynchronizerRepository
import com.advancednotes.data.api.repositories.ApiUserRepository
import com.advancednotes.data.database_notes.repositories.NotesFilesRepository
import com.advancednotes.data.database_notes.repositories.NotesRepository
import com.advancednotes.data.database_notes.repositories.TagsRepository
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.data.storage.repositories.NotesFilesStorageRepository
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.LocationUseCase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.PreSynchronizerUseCase
import com.advancednotes.domain.usecases.RecordMediaPlayerUseCase
import com.advancednotes.domain.usecases.SynchronizerUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import com.advancednotes.domain.usecases.TextToSpeechUseCase
import com.advancednotes.domain.usecases.UsersUseCase
import com.advancednotes.domain.usecases.impl.AuthenticationUseCaseImpl
import com.advancednotes.domain.usecases.impl.LocationUseCaseImpl
import com.advancednotes.domain.usecases.impl.NotesFilesUseCaseImpl
import com.advancednotes.domain.usecases.impl.NotesUseCaseImpl
import com.advancednotes.domain.usecases.impl.PreSynchronizerUseCaseImpl
import com.advancednotes.domain.usecases.impl.RecordMediaPlayerUseCaseImpl
import com.advancednotes.domain.usecases.impl.SynchronizerUseCaseImpl
import com.advancednotes.domain.usecases.impl.TagsUseCaseImpl
import com.advancednotes.domain.usecases.impl.TextToSpeechUseCaseImpl
import com.advancednotes.domain.usecases.impl.UsersUseCaseImpl
import com.advancednotes.utils.notifications.StickyNoteNotificationHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCaseImpl(
        apiAuthenticationRepository: ApiAuthenticationRepository,
        usersRepository: UsersRepository
    ): AuthenticationUseCase {
        return AuthenticationUseCaseImpl(
            apiAuthenticationRepository,
            usersRepository
        )
    }

    @Provides
    @Singleton
    fun provideUsersUseCaseImpl(
        apiUserRepository: ApiUserRepository,
        usersRepository: UsersRepository,
    ): UsersUseCase {
        return UsersUseCaseImpl(apiUserRepository, usersRepository)
    }

    @Provides
    @Singleton
    fun providePreSynchronizerUseCaseImpl(
        preSynchronizerRepository: ApiPreSynchronizerRepository,
        synchronizerUseCase: SynchronizerUseCase,
        authenticationUseCase: AuthenticationUseCase,
        notesUseCase: NotesUseCase,
        notesFilesUseCase: NotesFilesUseCase,
        tagsUseCase: TagsUseCase
    ): PreSynchronizerUseCase {
        return PreSynchronizerUseCaseImpl(
            preSynchronizerRepository,
            synchronizerUseCase,
            authenticationUseCase,
            notesUseCase,
            notesFilesUseCase,
            tagsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideSynchronizerUseCaseImpl(
        @ApplicationContext context: Context,
        apiSynchronizerRepository: ApiSynchronizerRepository,
        apiDownloaderRepository: ApiDownloaderRepository,
        authenticationUseCase: AuthenticationUseCase,
        notesUseCase: NotesUseCase,
        notesFilesUseCase: NotesFilesUseCase,
        tagsUseCase: TagsUseCase
    ): SynchronizerUseCase {
        return SynchronizerUseCaseImpl(
            context,
            apiSynchronizerRepository,
            apiDownloaderRepository,
            authenticationUseCase,
            notesUseCase,
            notesFilesUseCase,
            tagsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideNotesUseCaseImpl(
        notesRepository: NotesRepository,
        notesFilesUseCase: NotesFilesUseCase,
        stickyNoteNotificationHelper: StickyNoteNotificationHelper,
        gson: Gson
    ): NotesUseCase {
        return NotesUseCaseImpl(
            notesRepository,
            notesFilesUseCase,
            stickyNoteNotificationHelper,
            gson
        )
    }

    @Provides
    @Singleton
    fun provideNoteTagsUseCaseImpl(
        tagsRepository: TagsRepository
    ): TagsUseCase {
        return TagsUseCaseImpl(tagsRepository)
    }

    @Provides
    @Singleton
    fun provideNotesFilesUseCaseImpl(
        notesFilesRepository: NotesFilesRepository,
        notesFilesStorageRepository: NotesFilesStorageRepository
    ): NotesFilesUseCase {
        return NotesFilesUseCaseImpl(notesFilesRepository, notesFilesStorageRepository)
    }

    @Provides
    @Singleton
    fun provideLocationUseCaseImpl(
        @ApplicationContext context: Context
    ): LocationUseCase {
        return LocationUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun provideRecordMediaPlayerUseCaseImpl(
        @ApplicationContext context: Context
    ): RecordMediaPlayerUseCase {
        return RecordMediaPlayerUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun provideTextToSpeechUseCaseImpl(
        @ApplicationContext context: Context
    ): TextToSpeechUseCase {
        return TextToSpeechUseCaseImpl(context)
    }
}