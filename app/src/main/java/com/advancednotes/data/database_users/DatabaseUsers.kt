package com.advancednotes.data.database_users

import androidx.room.Database
import androidx.room.RoomDatabase
import com.advancednotes.data.database_users.dao.UsersDao
import com.advancednotes.data.database_users.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    /*autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]*/
)

abstract class DatabaseUsers : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}