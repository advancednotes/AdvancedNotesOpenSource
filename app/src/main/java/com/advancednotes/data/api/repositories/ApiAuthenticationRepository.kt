package com.advancednotes.data.api.repositories

import com.advancednotes.data.api.models.LoginFailedResponseModel
import com.advancednotes.data.api.models.LoginResponseModel
import com.advancednotes.data.api.models.VerifyEmailResponseModel

interface ApiAuthenticationRepository {

    fun verifyEmail(
        email: String,
        onResponse: (response: VerifyEmailResponseModel?) -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onResponse: (response: LoginResponseModel?) -> Unit,
        onLoginFailed: (response: LoginFailedResponseModel?) -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )

    fun loginWithRefreshToken(
        onResponse: (response: LoginResponseModel?) -> Unit,
        onLoginFailed: () -> Unit,
        onFailure: () -> Unit
    )

    fun logout(
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onFailure: () -> Unit
    )

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onResponse: (response: LoginResponseModel?) -> Unit,
        onRegisterFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )
}