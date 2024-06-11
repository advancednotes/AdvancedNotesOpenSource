package com.advancednotes.di

import com.advancednotes.data.api.interceptors.AccessInterceptor
import com.advancednotes.data.api.interceptors.BasicInterceptor
import com.advancednotes.data.api.interceptors.ForgotPasswordInterceptor
import com.advancednotes.data.api.interceptors.RefreshInterceptor
import com.advancednotes.di.qualifiers.ApiQualifiers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiClientModule {

    @Provides
    @Singleton
    @ApiQualifiers.BasicClient
    internal fun provideBasicClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiQualifiers.BasicInterceptor basicInterceptor: BasicInterceptor
    ): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(basicInterceptor)
            .build()

        return client
    }

    @Provides
    @Singleton
    @ApiQualifiers.AccessClient
    internal fun provideAccessClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiQualifiers.AccessInterceptor accessInterceptor: AccessInterceptor
    ): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(accessInterceptor)
            .build()

        return client
    }

    @Provides
    @Singleton
    @ApiQualifiers.RefreshClient
    internal fun provideRefreshClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiQualifiers.RefreshInterceptor refreshInterceptor: RefreshInterceptor
    ): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(refreshInterceptor)
            .build()

        return client
    }

    @Provides
    @Singleton
    @ApiQualifiers.ForgotPasswordClient
    internal fun provideForgotPasswordClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiQualifiers.ForgotPasswordInterceptor forgotPasswordInterceptor: ForgotPasswordInterceptor
    ): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(forgotPasswordInterceptor)
            .build()

        return client
    }
}