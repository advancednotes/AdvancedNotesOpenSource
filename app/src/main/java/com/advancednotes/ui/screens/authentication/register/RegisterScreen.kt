package com.advancednotes.ui.screens.authentication.register

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.buttons.MyButton
import com.advancednotes.ui.components.textfields.AdvancedTextField
import com.advancednotes.ui.components.textfields.EmailTextField
import com.advancednotes.ui.components.textfields.PasswordTextField
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    mainActivityViewModel: MainActivityViewModel,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val registerState: RegisterState by registerViewModel.registerState.collectAsState()

    var firstName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var lastName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var email: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var password: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.RegisterScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.RegisterScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )
        }
    }

    LaunchedEffect(registerViewModel.snackbarRegisterFailed) {
        registerViewModel.snackbarRegisterFailed.receiveAsFlow().collectLatest {
            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = context.getString(R.string.register_failed_message),
                    isError = true
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {}
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(registerViewModel.snackbarOnFailure) {
        registerViewModel.snackbarOnFailure.receiveAsFlow().collectLatest {
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
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AdvancedTextField(
                    label = stringResource(id = R.string.first_name_label),
                    value = firstName,
                    onValueChange = {
                        firstName = it
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                AdvancedTextField(
                    label = stringResource(id = R.string.last_name_label),
                    value = lastName,
                    onValueChange = {
                        lastName = it
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            EmailTextField(
                value = email,
                onValueChange = {
                    email = it
                    registerViewModel.emailIsValid(email.text)
                },
                supportingText = if (!registerState.emailIsValid && email.text.isNotEmpty()) {
                    stringResource(id = R.string.email_is_not_valid)
                } else "",
                isError = !registerState.emailIsValid && email.text.isNotEmpty(),
                isLoadingEmail = registerState.isLoadingEmail
            )

            PasswordTextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                }
            )

            MyButton(
                text = stringResource(id = R.string.register_button),
                onClick = {

                },
                modifier = Modifier.align(Alignment.End),
                enabled = !registerState.registerInProgress && registerState.emailIsValid && !registerState.emailAlreadyExists && password.text.isNotEmpty()
            )
        }
    }
}