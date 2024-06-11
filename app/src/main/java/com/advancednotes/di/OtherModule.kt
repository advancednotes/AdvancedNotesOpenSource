package com.advancednotes.di

import android.content.Context
import com.advancednotes.data.mediastore.MediaStoreHelper
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.utils.notifications.DownloadFileNotificationHelper
import com.advancednotes.utils.notifications.DownloadedFileNotificationHelper
import com.advancednotes.utils.notifications.NoteLocationNotificationHelper
import com.advancednotes.utils.notifications.NoteRecordAudioNotificationHelper
import com.advancednotes.utils.notifications.NoteReminderNotificationHelper
import com.advancednotes.utils.notifications.StickyNoteNotificationHelper
import com.advancednotes.utils.type_adapters.AdvancedContentTypeAdapter
import com.advancednotes.utils.user_agent.UserAgentHelper
import com.advancednotes.utils.workers.WorkerManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OtherModule {

    @Provides
    @Singleton
    fun provideWorkerManager(
        @ApplicationContext context: Context
    ): WorkerManager {
        return WorkerManager(context)
    }

    @Provides
    @Singleton
    fun provideNoteLocationNotificationHelper(
        @ApplicationContext context: Context,
    ): NoteLocationNotificationHelper {
        return NoteLocationNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideNoteReminderNotificationHelper(
        @ApplicationContext context: Context,
    ): NoteReminderNotificationHelper {
        return NoteReminderNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideStickyNoteNotificationHelper(
        @ApplicationContext context: Context,
    ): StickyNoteNotificationHelper {
        return StickyNoteNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideNoteRecordAudioNotificationHelper(
        @ApplicationContext context: Context,
    ): NoteRecordAudioNotificationHelper {
        return NoteRecordAudioNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideDownloadFileNotificationHelper(
        @ApplicationContext context: Context,
    ): DownloadFileNotificationHelper {
        return DownloadFileNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideDownloadedFileNotificationHelper(
        @ApplicationContext context: Context,
    ): DownloadedFileNotificationHelper {
        return DownloadedFileNotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideMediaStoreHelper(
        @ApplicationContext context: Context,
    ): MediaStoreHelper {
        return MediaStoreHelper(context)
    }

    @Provides
    @Singleton
    fun provideUserAgentHelper(
        @ApplicationContext context: Context,
    ): UserAgentHelper {
        return UserAgentHelper(context)
    }

    @Provides
    @Singleton
    fun provideGson(
    ): Gson = GsonBuilder()
        .registerTypeAdapter(AdvancedContentType::class.java, AdvancedContentTypeAdapter())
        .serializeNulls()
        .create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(
        gson: Gson
    ): Converter.Factory = GsonConverterFactory.create(gson)
}