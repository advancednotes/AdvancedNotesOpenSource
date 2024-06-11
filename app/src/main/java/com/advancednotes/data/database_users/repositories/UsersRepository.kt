package com.advancednotes.data.database_users.repositories

import com.advancednotes.data.database_users.entities.UserEntity

interface UsersRepository {

    fun getUser(): UserEntity?
    fun insertUser(userEntity: UserEntity): Long
    fun updateUser(userEntity: UserEntity)
    fun updateFirstName(userId: Long, firstName: String)
    fun updateLastName(userId: Long, lastName: String)
    fun updateAccessToken(userId: Long, accessToken: String?)
    fun updateRefreshToken(userId: Long, refreshToken: String?)
    fun deleteUser()
}