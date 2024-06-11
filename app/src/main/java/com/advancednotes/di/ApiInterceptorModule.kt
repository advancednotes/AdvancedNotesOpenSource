package com.advancednotes.di

import android.content.Context
import com.advancednotes.data.api.interceptors.AccessInterceptor
import com.advancednotes.data.api.interceptors.BasicInterceptor
import com.advancednotes.data.api.interceptors.ForgotPasswordInterceptor
import com.advancednotes.data.api.interceptors.RefreshInterceptor
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.di.qualifiers.ApiQualifiers
import com.advancednotes.utils.logs.LogsHelper.logd
import com.advancednotes.utils.user_agent.UserAgentHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiInterceptorModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                logd("HTTP $message")
            }
        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    @ApiQualifiers.BasicInterceptor
    fun provideBasicInterceptor(
        userAgentHelper: UserAgentHelper
    ): BasicInterceptor {
        return BasicInterceptor(userAgentHelper)
    }

    @Provides
    @Singleton
    @ApiQualifiers.AccessInterceptor
    fun provideAccessInterceptor(
        usersRepository: UsersRepository,
        userAgentHelper: UserAgentHelper
    ): AccessInterceptor {
        return AccessInterceptor(usersRepository, userAgentHelper)
    }

    @Provides
    @Singleton
    @ApiQualifiers.RefreshInterceptor
    fun provideRefreshInterceptor(
        usersRepository: UsersRepository,
        userAgentHelper: UserAgentHelper
    ): RefreshInterceptor {
        return RefreshInterceptor(usersRepository, userAgentHelper)
    }

    @Provides
    @Singleton
    @ApiQualifiers.ForgotPasswordInterceptor
    fun provideForgotPasswordInterceptor(
        @ApplicationContext context: Context,
        userAgentHelper: UserAgentHelper
    ): ForgotPasswordInterceptor {
        return ForgotPasswordInterceptor(context, userAgentHelper)
    }
}