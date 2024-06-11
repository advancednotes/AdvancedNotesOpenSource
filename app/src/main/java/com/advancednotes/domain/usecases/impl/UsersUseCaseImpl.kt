package com.advancednotes.domain.usecases.impl

import com.advancednotes.data.api.repositories.ApiUserRepository
import com.advancednotes.data.database_users.entities.UserEntity
import com.advancednotes.data.database_users.entities.toDomain
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.domain.models.User
import com.advancednotes.domain.usecases.UsersUseCase
import javax.inject.Inject

class UsersUseCaseImpl @Inject constructor(
    private val apiUserRepository: ApiUserRepository,
    private val usersRepository: UsersRepository,
) : UsersUseCase {

    override fun getUser(): User? {
        return usersRepository.getUser()?.toDomain()
    }

    override fun updateFirstName(firstName: String) {
        apiUserRepository.updateFirstName(
            firstName = firstName,
            onResponse = {
                val userEntity: UserEntity? = usersRepository.getUser()

                if (userEntity != null) {
                    usersRepository.updateFirstName(
                        userEntity.id,
                        firstName
                    )
                }
            },
            onResponseFailed = {},
            onBadRequest = {},
            onFailure = {}
        )
    }

    override fun updateLastName(lastName: String) {
        apiUserRepository.updateLastName(
            lastName = lastName,
            onResponse = {
                val userEntity: UserEntity? = usersRepository.getUser()

                if (userEntity != null) {
                    usersRepository.updateLastName(
                        userEntity.id,
                        lastName
                    )
                }
            },
            onResponseFailed = {},
            onBadRequest = {},
            onFailure = {}
        )
    }

    override fun updatePassword(password: String) {
        apiUserRepository.updatePassword(
            password = password,
            onResponse = {},
            onResponseFailed = {},
            onBadRequest = {},
            onFailure = {}
        )
    }
}