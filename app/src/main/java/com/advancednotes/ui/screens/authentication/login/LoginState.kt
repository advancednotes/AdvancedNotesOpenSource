package com.advancednotes.ui.screens.authentication.login

import com.advancednotes.domain.models.User

data class LoginState(
    val currentUser: User? = null,
    val loggingInProgress: Boolean = false,
    val emailIsValid: Boolean = true,
    val availableAttempts: Int = 0,
    val waitingTimeInMs: Long? = null,
)