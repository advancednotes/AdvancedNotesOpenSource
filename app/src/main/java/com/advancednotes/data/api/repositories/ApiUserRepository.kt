package com.advancednotes.data.api.repositories

interface ApiUserRepository {

    fun updateFirstName(
        firstName: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )

    fun updateLastName(
        lastName: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )

    fun updatePassword(
        password: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    )
}