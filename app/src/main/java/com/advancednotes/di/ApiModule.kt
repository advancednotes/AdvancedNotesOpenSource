package com.advancednotes.di

import com.advancednotes.BuildConfig
import com.advancednotes.di.qualifiers.ApiQualifiers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    @ApiQualifiers.BasicRetrofit
    fun provideBasicRetrofit(
        @ApiQualifiers.BasicClient client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}/")
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    @Singleton
    @Provides
    @ApiQualifiers.AccessRetrofit
    fun provideAccessRetrofit(
        @ApiQualifiers.AccessClient client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}/")
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    @Singleton
    @Provides
    @ApiQualifiers.RefreshRetrofit
    fun provideRefreshRetrofit(
        @ApiQualifiers.RefreshClient client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}/")
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    @Singleton
    @Provides
    @ApiQualifiers.ForgotPasswordRetrofit
    fun provideForgotPasswordRetrofit(
        @ApiQualifiers.ForgotPasswordClient client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}/")
        .client(client)
        .addConverterFactory(converterFactory)
        .build()
}