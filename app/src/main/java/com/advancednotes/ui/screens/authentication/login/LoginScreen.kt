package com.advancednotes.ui.screens.authentication.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.buttons.MyButton
import com.advancednotes.ui.components.buttons.MyExtendedFAB
import com.advancednotes.ui.components.textfields.AdvancedTextField
import com.advancednotes.ui.components.textfields.PasswordTextField
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    mainActivityViewModel: MainActivityViewModel,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val loginState: LoginState by loginViewModel.loginState.collectAsState()

    var email: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var password: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.LoginScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.LoginScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )
        }
    }

    LaunchedEffect(loginState.currentUser) {
        if (loginState.currentUser != null) {
            email = email.copy(text = loginState.currentUser!!.email)
        }
    }

    LaunchedEffect(loginViewModel.snackbarLoginFailed) {
        loginViewModel.snackbarLoginFailed.receiveAsFlow().collectLatest {
            val message: String = when {
                loginState.availableAttempts < 1 -> context.getString(R.string.login_locked_message)
                loginState.availableAttempts <= 5 -> context.getString(
                    R.string.login_attempts_message,
                    loginState.availableAttempts
                )

                else -> context.getString(R.string.login_failed_message)
            }

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = true
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {}
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(loginViewModel.snackbarUserNotExists) {
        loginViewModel.snackbarUserNotExists.receiveAsFlow().collectLatest {
            val message: String = context.getString(R.string.login_user_not_exists_message)

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = true
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {}
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(loginViewModel.snackbarOnFailure) {
        loginViewModel.snackbarOnFailure.receiveAsFlow().collectLatest {
            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = context.getString(R.string.error_try_again_later),
                    isError = true
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {}
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    ScreenContainer {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
        ) {
            AdvancedTextField(
                label = stringResource(id = R.string.email_label),
                value = email,
                onValueChange = {
                    email = it
                    loginViewModel.verifyEmail(email.text)
                },
                modifier = Modifier.fillMaxWidth(),
                supportingText = if (!loginState.emailIsValid && email.text.isNotEmpty()) {
                    stringResource(id = R.string.email_is_not_valid)
                } else "",
                isError = !loginState.emailIsValid && email.text.isNotEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )

            PasswordTextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                },
                onClickForgotPassword = {

                }
            )

            MyButton(
                text = stringResource(id = R.string.login_button),
                onClick = {
                    loginViewModel.login(
                        email = email.text,
                        password = password.text,
                        onLoginSuccess = { user ->
                            mainScope.launch {
                                mainActivityViewModel.setUserState(user = user)
                            }

                            navController.popBackStack()
                        }
                    )
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !loginState.loggingInProgress && loginState.emailIsValid && password.text.isNotEmpty()
            )
        }

        MyExtendedFAB(
            icon = { Icon(Icons.Filled.Add, null) },
            text = { MyText(text = stringResource(id = R.string.screen_register_label)) },
            onClick = {
                navController.myNavigate(
                    route = Screen.RegisterScreen.route,
                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                )
            },
            expanded = true,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}