package com.advancednotes.domain.usecases

import com.advancednotes.domain.models.User

interface UsersUseCase {

    fun getUser(): User?
    fun updateFirstName(firstName: String)
    fun updateLastName(lastName: String)
    fun updatePassword(password: String)
}