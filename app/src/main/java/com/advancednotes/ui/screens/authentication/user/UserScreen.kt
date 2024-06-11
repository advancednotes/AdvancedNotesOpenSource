package com.advancednotes.ui.screens.authentication.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import com.advancednotes.domain.models.User
import com.advancednotes.ui.components.ColumnWithLabel
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.buttons.MyButton
import com.advancednotes.ui.components.textfields.AdvancedTextField
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.components.user.ChangeUserPassword
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    mainActivityViewModel: MainActivityViewModel,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val userScreenState: UserScreenState by userViewModel.userScreenState.collectAsState()
    val userState: State<User?> = mainActivityViewModel.userState.collectAsState()

    var firstName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var lastName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var email: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.UserScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.UserScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )
        }
    }

    LaunchedEffect(userState.value) {
        if (userState.value != null) {
            userState.value?.let {
                firstName = TextFieldValue(text = it.firstName)
                lastName = TextFieldValue(text = it.lastName)
                email = TextFieldValue(text = it.email)
            }
        }
    }

    ScreenContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
        ) {
            ColumnWithLabel(
                label = stringResource(id = R.string.user_personal_data_label)
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT),
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

                AdvancedTextField(
                    label = stringResource(id = R.string.email_label),
                    value = email,
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    singleLine = true
                )

                MyButton(
                    text = stringResource(id = R.string.save_button),
                    onClick = {

                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !userScreenState.saveInProgress
                )
            }

            ColumnWithLabel(
                label = stringResource(id = R.string.user_security_label)
            ) {
                ChangeUserPassword()
            }
        }
    }
}