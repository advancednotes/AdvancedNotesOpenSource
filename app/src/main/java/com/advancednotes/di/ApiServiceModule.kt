package com.advancednotes.di

import com.advancednotes.data.api.services.DownloaderService
import com.advancednotes.data.api.services.LoginAccessService
import com.advancednotes.data.api.services.LoginBasicService
import com.advancednotes.data.api.services.LoginRefreshService
import com.advancednotes.data.api.services.PreSynchronizerService
import com.advancednotes.data.api.services.SynchronizerService
import com.advancednotes.data.api.services.UserService
import com.advancednotes.di.qualifiers.ApiQualifiers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideLoginBasicService(
        @ApiQualifiers.BasicRetrofit retrofit: Retrofit
    ): LoginBasicService = retrofit.create(LoginBasicService::class.java)

    @Provides
    @Singleton
    fun provideLoginAccessService(
        @ApiQualifiers.AccessRetrofit retrofit: Retrofit
    ): LoginAccessService = retrofit.create(LoginAccessService::class.java)

    @Provides
    @Singleton
    fun provideLoginRefreshService(
        @ApiQualifiers.RefreshRetrofit retrofit: Retrofit
    ): LoginRefreshService = retrofit.create(LoginRefreshService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        @ApiQualifiers.AccessRetrofit retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providePreSynchronizerService(
        @ApiQualifiers.AccessRetrofit retrofit: Retrofit
    ): PreSynchronizerService = retrofit.create(PreSynchronizerService::class.java)

    @Provides
    @Singleton
    fun provideSynchronizerService(
        @ApiQualifiers.AccessRetrofit retrofit: Retrofit
    ): SynchronizerService = retrofit.create(SynchronizerService::class.java)

    @Provides
    @Singleton
    fun provideDownloaderService(
        @ApiQualifiers.AccessRetrofit retrofit: Retrofit
    ): DownloaderService = retrofit.create(DownloaderService::class.java)
}