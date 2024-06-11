package com.advancednotes.domain.usecases

import com.advancednotes.domain.models.User

interface AuthenticationUseCase {

    fun hasUser(): Boolean
    fun hasAccessToken(): Boolean
    fun hasRefreshToken(): Boolean

    fun verifyEmailFromApi(
        email: String,
        onResponse: (emailAlreadyExists: Boolean) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (user: User) -> Unit,
        onLoginFailed: (userExists: Boolean, availableAttempts: Int, waitingTimeInMs: Long?) -> Unit,
        onFailure: () -> Unit
    )

    fun loginWithRefreshToken(
        onLoginSuccess: (user: User) -> Unit,
        onFailure: (() -> Unit)? = null
    )

    fun logout(
        partialLogout: Boolean,
        onLogout: (() -> Unit)? = null
    )

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onRegisterSuccess: (user: User) -> Unit,
        onRegisterFailed: () -> Unit,
        onFailure: () -> Unit
    )
}