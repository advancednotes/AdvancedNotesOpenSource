package com.advancednotes.ui.screens.authentication.register

data class RegisterState(
    val registerInProgress: Boolean = false,
    val isLoadingEmail: Boolean = false,
    val emailIsValid: Boolean = false,
    val emailAlreadyExists: Boolean = false,
)