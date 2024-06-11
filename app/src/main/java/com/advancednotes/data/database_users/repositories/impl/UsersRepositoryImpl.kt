package com.advancednotes.data.database_users.repositories.impl

import com.advancednotes.data.database_users.dao.UsersDao
import com.advancednotes.data.database_users.entities.UserEntity
import com.advancednotes.data.database_users.repositories.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao
) : UsersRepository {

    override fun getUser(): UserEntity? {
        return usersDao.getUser()
    }

    override fun insertUser(userEntity: UserEntity): Long {
        return usersDao.insertUser(userEntity)
    }

    override fun updateUser(userEntity: UserEntity) {
        usersDao.updateUser(userEntity)
    }

    override fun updateFirstName(userId: Long, firstName: String) {
        usersDao.updateFirstName(userId, firstName)
    }

    override fun updateLastName(userId: Long, lastName: String) {
        usersDao.updateLastName(userId, lastName)
    }

    override fun updateAccessToken(userId: Long, accessToken: String?) {
        usersDao.updateAccessToken(userId, accessToken)
    }

    override fun updateRefreshToken(userId: Long, refreshToken: String?) {
        usersDao.updateRefreshToken(userId, refreshToken)
    }

    override fun deleteUser() {
        usersDao.deleteUser()
    }
}