package com.advancednotes.ui.screens.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.User
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.utils.email.EmailHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    val snackbarRegisterFailed = Channel<Unit>(Channel.CONFLATED)
    val snackbarOnFailure = Channel<Unit>(Channel.CONFLATED)

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onRegisterSuccess: (user: User) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerState.value = _registerState.value.copy(
                registerInProgress = true
            )

            authenticationUseCase.register(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                onRegisterSuccess = { user ->
                    launch(Dispatchers.Main) {
                        _registerState.value = _registerState.value.copy(
                            registerInProgress = false
                        )

                        onRegisterSuccess(user)
                    }
                },
                onRegisterFailed = {
                    showSnackbarRegisterFailed()
                },
                onFailure = {
                    showSnackbarOnFailure()
                }
            )
        }
    }

    fun emailIsValid(
        email: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerState.value = _registerState.value.copy(
                emailIsValid = EmailHelper.emailIsValid(email)
            )

            if (!_registerState.value.isLoadingEmail) {
                verifyEmail(email)
            }
        }
    }

    private fun verifyEmail(
        email: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerState.value = _registerState.value.copy(
                isLoadingEmail = true
            )

            if (_registerState.value.emailIsValid) {
                authenticationUseCase.verifyEmailFromApi(
                    email = email,
                    onResponse = { emailAlreadyExists ->
                        _registerState.value = _registerState.value.copy(
                            isLoadingEmail = false,
                            emailAlreadyExists = emailAlreadyExists
                        )
                    }
                )
            }
        }
    }

    private fun showSnackbarRegisterFailed() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarRegisterFailed.trySend(Unit)
        }
    }

    private fun showSnackbarOnFailure() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarOnFailure.trySend(Unit)
        }
    }
}