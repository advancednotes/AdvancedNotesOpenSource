package com.advancednotes.di

import com.advancednotes.utils.services.LocationService
import com.advancednotes.utils.services.RecordAudioService
import com.advancednotes.utils.services.SetRemindersService
import com.advancednotes.utils.services.SetStickyNotesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @Provides
    fun provideLocationService(): LocationService {
        return LocationService()
    }

    @Provides
    fun provideSetRemindersService(): SetRemindersService {
        return SetRemindersService()
    }

    @Provides
    fun provideSetStickyNotesService(): SetStickyNotesService {
        return SetStickyNotesService()
    }

    @Provides
    fun provideRecordAudioService(): RecordAudioService {
        return RecordAudioService()
    }
}