package com.advancednotes.ui.screens.authentication.user

import androidx.lifecycle.ViewModel
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.UsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val usersUseCase: UsersUseCase
) : ViewModel() {

    private val _userScreenState = MutableStateFlow(UserScreenState())
    val userScreenState: StateFlow<UserScreenState> = _userScreenState.asStateFlow()

    fun updateFirstName(firstName: String) {
        usersUseCase.updateFirstName(firstName)
    }

    fun updateLastName(lastName: String) {
        usersUseCase.updateLastName(lastName)
    }
}