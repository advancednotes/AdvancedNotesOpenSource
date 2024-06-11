package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.data.api.models.UserModel
import com.advancednotes.data.database_users.entities.UserEntity

@Keep
data class User(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val accessToken: String?,
    val refreshToken: String?
)

fun User.toApi(): UserModel {
    return UserModel(
        email = email,
        firstName = firstName,
        lastName = lastName
    )
}

fun User.toDatabase(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}