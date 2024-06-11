package com.advancednotes.di

import android.content.Context
import androidx.room.Room
import com.advancednotes.data.database_users.DatabaseUsers
import com.advancednotes.data.database_users.dao.UsersDao
import com.advancednotes.di.qualifiers.DatabaseQualifiers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseUsersModule {

    @Provides
    fun provideUsersDao(
        @DatabaseQualifiers.DatabaseUsers databaseUsers: DatabaseUsers
    ): UsersDao {
        return databaseUsers.usersDao()
    }

    @Provides
    @Singleton
    @DatabaseQualifiers.DatabaseUsers
    fun provideDatabaseUsers(
        @ApplicationContext context: Context
    ): DatabaseUsers {
        return Room.databaseBuilder(context, DatabaseUsers::class.java, "users.db").build()
    }
}