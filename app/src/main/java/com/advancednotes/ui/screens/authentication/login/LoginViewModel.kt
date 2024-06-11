package com.advancednotes.ui.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.User
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.UsersUseCase
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
class LoginViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val usersUseCase: UsersUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    val snackbarLoginFailed = Channel<Unit>(Channel.CONFLATED)
    val snackbarUserNotExists = Channel<Unit>(Channel.CONFLATED)
    val snackbarOnFailure = Channel<Unit>(Channel.CONFLATED)

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.value = _loginState.value.copy(
                currentUser = usersUseCase.getUser()
            )
        }
    }

    fun login(
        email: String,
        password: String,
        onLoginSuccess: (user: User) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.emit(
                _loginState.value.copy(
                    loggingInProgress = true
                )
            )

            authenticationUseCase.loginWithEmailAndPassword(
                email = email,
                password = password,
                onLoginSuccess = { user ->
                    launch(Dispatchers.Main) {
                        _loginState.value = _loginState.value.copy(
                            loggingInProgress = false
                        )

                        onLoginSuccess(user)
                    }
                },
                onLoginFailed = { userExists, availableAttempts, waitingTimeInMs ->
                    launch(Dispatchers.Main) {
                        if (userExists) {
                            showSnackbarLoginFailed()
                        } else {
                            showSnackbarUserNotExists()
                        }

                        _loginState.value = _loginState.value.copy(
                            loggingInProgress = false,
                            availableAttempts = availableAttempts,
                            waitingTimeInMs = waitingTimeInMs
                        )
                    }
                },
                onFailure = {
                    launch(Dispatchers.Main) {
                        showSnackbarOnFailure()

                        _loginState.value = _loginState.value.copy(
                            loggingInProgress = false
                        )
                    }
                }
            )
        }
    }

    fun verifyEmail(email: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _loginState.value = _loginState.value.copy(
                emailIsValid = EmailHelper.emailIsValid(email)
            )
        }
    }

    private fun showSnackbarLoginFailed() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarLoginFailed.trySend(Unit)
        }
    }

    private fun showSnackbarUserNotExists() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarUserNotExists.trySend(Unit)
        }
    }

    private fun showSnackbarOnFailure() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarOnFailure.trySend(Unit)
        }
    }
}