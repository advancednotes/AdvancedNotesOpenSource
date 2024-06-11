package com.advancednotes.data.database_users.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.advancednotes.data.database_users.entities.UserEntity

@Dao
interface UsersDao {

    /**
     * GET
     * */
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): UserEntity?

    /**
     * INSERT
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity): Long

    /**
     * UPDATE
     * */
    @Update
    fun updateUser(userEntity: UserEntity)

    @Query("UPDATE users SET first_name = :firstName WHERE id = :userId")
    fun updateFirstName(userId: Long, firstName: String)

    @Query("UPDATE users SET last_name = :lastName WHERE id = :userId")
    fun updateLastName(userId: Long, lastName: String)

    @Query("UPDATE users SET access_token = :accessToken WHERE id = :userId")
    fun updateAccessToken(userId: Long, accessToken: String?)

    @Query("UPDATE users SET refresh_token = :refreshToken WHERE id = :userId")
    fun updateRefreshToken(userId: Long, refreshToken: String?)

    /**
     * DELETE
     * */
    @Query("DELETE FROM users")
    fun deleteUser()
}