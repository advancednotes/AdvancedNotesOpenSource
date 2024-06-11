package com.advancednotes.domain.usecases.impl

import com.advancednotes.data.api.repositories.ApiAuthenticationRepository
import com.advancednotes.data.database_users.entities.UserEntity
import com.advancednotes.data.database_users.entities.toDomain
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.domain.models.User
import com.advancednotes.domain.usecases.AuthenticationUseCase
import javax.inject.Inject

class AuthenticationUseCaseImpl @Inject constructor(
    private val apiAuthenticationRepository: ApiAuthenticationRepository,
    private val usersRepository: UsersRepository
) : AuthenticationUseCase {

    override fun hasUser(): Boolean {
        return usersRepository.getUser() != null
    }

    override fun hasAccessToken(): Boolean {
        return usersRepository.getUser()?.accessToken?.isNotEmpty() ?: false
    }

    override fun hasRefreshToken(): Boolean {
        return usersRepository.getUser()?.refreshToken?.isNotEmpty() ?: false
    }

    override fun verifyEmailFromApi(
        email: String,
        onResponse: (emailAlreadyExists: Boolean) -> Unit,
        onBadRequest: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        apiAuthenticationRepository.verifyEmail(
            email = email,
            onResponse = { response ->
                if (response != null) {
                    onResponse(response.emailAlreadyExists)
                } else {
                    onFailure?.invoke()
                }
            },
            onBadRequest = {
                onBadRequest?.invoke()
            },
            onFailure = {
                onFailure?.invoke()
            }
        )
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (user: User) -> Unit,
        onLoginFailed: (userExists: Boolean, availableAttempts: Int, waitingTimeInMs: Long?) -> Unit,
        onFailure: () -> Unit
    ) {
        apiAuthenticationRepository.loginWithEmailAndPassword(
            email = email,
            password = password,
            onResponse = { response ->
                if (response != null) {
                    val userEntity: UserEntity? = usersRepository.getUser()

                    if (userEntity != null) {
                        val userToUpdate = UserEntity(
                            id = userEntity.id,
                            email = response.user.email,
                            firstName = response.user.firstName,
                            lastName = response.user.lastName,
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )

                        usersRepository.updateUser(userToUpdate)
                        onLoginSuccess(userToUpdate.toDomain())
                    } else {
                        val userToInsert = UserEntity(
                            id = 0,
                            email = response.user.email,
                            firstName = response.user.firstName,
                            lastName = response.user.lastName,
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )

                        usersRepository.insertUser(userToInsert)
                        onLoginSuccess(userToInsert.toDomain())
                    }
                } else {
                    onFailure()
                }
            },
            onLoginFailed = { response ->
                if (response != null) {
                    onLoginFailed(
                        response.userExists,
                        response.availableAttempts,
                        response.waitingTimeInMs
                    )
                } else {
                    onFailure()
                }
            },
            onBadRequest = {
                onFailure()
            },
            onFailure = {
                onFailure()
            }
        )
    }

    override fun loginWithRefreshToken(
        onLoginSuccess: (user: User) -> Unit,
        onFailure: (() -> Unit)?
    ) {
        val userEntity: UserEntity? = usersRepository.getUser()

        if (userEntity != null) {
            if (hasRefreshToken()) {
                apiAuthenticationRepository.loginWithRefreshToken(
                    onResponse = { response ->
                        if (response != null) {
                            val userToUpdate = UserEntity(
                                id = userEntity.id,
                                email = response.user.email,
                                firstName = response.user.firstName,
                                lastName = response.user.lastName,
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken
                            )

                            usersRepository.updateUser(userToUpdate)
                            onLoginSuccess(userToUpdate.toDomain())
                        } else {
                            onFailure?.invoke()
                        }
                    },
                    onLoginFailed = {
                        logout(partialLogout = true)
                    },
                    onFailure = {
                        onFailure?.invoke()
                    }
                )
            }
        }
    }

    override fun logout(
        partialLogout: Boolean,
        onLogout: (() -> Unit)?
    ) {
        val userEntity: UserEntity? = usersRepository.getUser()

        if (userEntity != null) {
            if (hasAccessToken()) {
                apiAuthenticationRepository.logout(
                    onResponse = {},
                    onResponseFailed = {},
                    onFailure = {}
                )
            }

            if (partialLogout) {
                usersRepository.updateAccessToken(userEntity.id, null)
                usersRepository.updateRefreshToken(userEntity.id, null)
            } else {
                usersRepository.deleteUser()
            }

            onLogout?.invoke()
        }
    }

    override fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onRegisterSuccess: (user: User) -> Unit,
        onRegisterFailed: () -> Unit,
        onFailure: () -> Unit
    ) {
        apiAuthenticationRepository.register(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            onResponse = { response ->
                if (response != null) {
                    val userEntity: UserEntity? = usersRepository.getUser()

                    if (userEntity != null) {
                        val userToUpdate = UserEntity(
                            id = userEntity.id,
                            email = response.user.email,
                            firstName = response.user.firstName,
                            lastName = response.user.lastName,
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )

                        usersRepository.updateUser(userToUpdate)
                        onRegisterSuccess(userToUpdate.toDomain())
                    } else {
                        val userToInsert = UserEntity(
                            id = 0,
                            email = response.user.email,
                            firstName = response.user.firstName,
                            lastName = response.user.lastName,
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )

                        usersRepository.insertUser(userToInsert)
                        onRegisterSuccess(userToInsert.toDomain())
                    }
                } else {
                    onFailure()
                }
            },
            onRegisterFailed = {
                onRegisterFailed()
            },
            onBadRequest = {
                onFailure()
            },
            onFailure = {
                onFailure()
            }
        )
    }
}