package com.advancednotes.di

import android.content.Context
import com.advancednotes.data.api.repositories.ApiAuthenticationRepository
import com.advancednotes.data.api.repositories.ApiDownloaderRepository
import com.advancednotes.data.api.repositories.ApiPreSynchronizerRepository
import com.advancednotes.data.api.repositories.ApiSynchronizerRepository
import com.advancednotes.data.api.repositories.ApiUserRepository
import com.advancednotes.data.api.repositories.impl.ApiAuthenticationRepositoryImpl
import com.advancednotes.data.api.repositories.impl.ApiDownloaderRepositoryImpl
import com.advancednotes.data.api.repositories.impl.ApiPreSynchronizerRepositoryImpl
import com.advancednotes.data.api.repositories.impl.ApiSynchronizerRepositoryImpl
import com.advancednotes.data.api.repositories.impl.ApiUserRepositoryImpl
import com.advancednotes.data.api.services.DownloaderService
import com.advancednotes.data.api.services.LoginAccessService
import com.advancednotes.data.api.services.LoginBasicService
import com.advancednotes.data.api.services.LoginRefreshService
import com.advancednotes.data.api.services.PreSynchronizerService
import com.advancednotes.data.api.services.SynchronizerService
import com.advancednotes.data.api.services.UserService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiRepositoryModule {

    @Provides
    @Singleton
    fun provideApiAuthenticationRepositoryImpl(
        loginBasicService: LoginBasicService,
        loginAccessService: LoginAccessService,
        loginRefreshService: LoginRefreshService,
        gson: Gson
    ): ApiAuthenticationRepository {
        return ApiAuthenticationRepositoryImpl(
            loginBasicService,
            loginAccessService,
            loginRefreshService,
            gson
        )
    }

    @Provides
    @Singleton
    fun provideApiUserRepositoryImpl(
        userService: UserService,
        gson: Gson
    ): ApiUserRepository {
        return ApiUserRepositoryImpl(userService, gson)
    }

    @Provides
    @Singleton
    fun providePreApiSynchronizerRepositoryImpl(
        preSynchronizerService: PreSynchronizerService,
        gson: Gson
    ): ApiPreSynchronizerRepository {
        return ApiPreSynchronizerRepositoryImpl(preSynchronizerService, gson)
    }

    @Provides
    @Singleton
    fun provideApiSynchronizerRepositoryImpl(
        synchronizerService: SynchronizerService,
        gson: Gson
    ): ApiSynchronizerRepository {
        return ApiSynchronizerRepositoryImpl(
            synchronizerService,
            gson
        )
    }

    @Provides
    @Singleton
    fun provideApiDownloaderRepositoryImpl(
        @ApplicationContext context: Context,
        downloaderService: DownloaderService
    ): ApiDownloaderRepository {
        return ApiDownloaderRepositoryImpl(
            context,
            downloaderService
        )
    }
}