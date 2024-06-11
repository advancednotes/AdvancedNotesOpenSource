package com.advancednotes.di

import com.advancednotes.data.database_users.dao.UsersDao
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.data.database_users.repositories.impl.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseUsersRepositoryModule {

    @Provides
    @Singleton
    fun provideUsersRepositoryImpl(
        usersDao: UsersDao
    ): UsersRepository {
        return UsersRepositoryImpl(usersDao)
    }
}