package com.advancednotes.ui.components.navigation_drawer

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.advancednotes.R
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.User
import com.advancednotes.ui.components.MyDropdownMenu
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginDropdown(
    navController: NavHostController,
    drawerState: DrawerState,
    drawerScope: CoroutineScope,
    mainActivityViewModel: MainActivityViewModel,
    modifier: Modifier = Modifier
) {
    val context: Context = LocalContext.current

    val userState: State<User?> = mainActivityViewModel.userState.collectAsState()

    var userExists: Boolean by remember { mutableStateOf(false) }
    var isLoggedIn: Boolean by remember { mutableStateOf(false) }
    var dropDownExpanded: Boolean by remember { mutableStateOf(false) }

    var dropDownItems: List<ContextMenuItem> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(userState.value) {
        userExists = userState.value != null
        isLoggedIn = userState.value?.accessToken != null

        dropDownItems = if (userExists) {
            if (isLoggedIn) {
                listOf(
                    ContextMenuItem(
                        text = context.getString(R.string.screen_user_label),
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            navController.myNavigate(
                                route = Screen.UserScreen.route,
                                afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                            )
                        },
                        icon = Icons.Default.Person
                    ),
                    ContextMenuItem(
                        text = context.getString(R.string.logout),
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            mainActivityViewModel.logout(partialLogout = false)
                        },
                        icon = Icons.AutoMirrored.Filled.Logout
                    )
                )
            } else {
                listOf(
                    ContextMenuItem(
                        text = context.getString(R.string.screen_login_label),
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            navController.myNavigate(
                                route = Screen.LoginScreen.route,
                                afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                            )
                        },
                        icon = Icons.AutoMirrored.Filled.Login
                    ),
                    ContextMenuItem(
                        text = context.getString(R.string.logout),
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            mainActivityViewModel.logout(partialLogout = false)
                        },
                        icon = Icons.AutoMirrored.Filled.Logout
                    )
                )
            }
        } else {
            listOf(
                ContextMenuItem(
                    text = context.getString(R.string.screen_login_label),
                    onClick = {
                        drawerScope.launch { drawerState.close() }
                        navController.myNavigate(
                            route = Screen.LoginScreen.route,
                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                        )
                    },
                    icon = Icons.AutoMirrored.Filled.Login
                )
            )
        }
    }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopEnd)
            .clickable(
                onClick = {
                    if (!userExists) {
                        drawerScope.launch { drawerState.close() }
                        navController.myNavigate(
                            route = Screen.LoginScreen.route,
                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                        )
                    } else {
                        dropDownExpanded = !dropDownExpanded
                    }
                }
            )
    ) {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            if (!userExists) {
                MyText(
                    text = stringResource(id = R.string.login_text)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SessionStatus(isLoggedIn = isLoggedIn)

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyText(
                            text = userState.value?.email ?: ""
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            if (dropDownExpanded) {
                                Icons.Default.KeyboardArrowUp
                            } else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
            }
        }

        MyDropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = {
                dropDownExpanded = false
            },
            offset = DpOffset(x = 0.dp, y = 0.dp)
        ) {
            dropDownItems.forEach { dropdownItem ->
                DropdownMenuItem(
                    text = {
                        MyText(
                            text = dropdownItem.text,
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                    },
                    onClick = {
                        dropdownItem.onClick()
                        dropDownExpanded = false
                    },
                    leadingIcon = {
                        dropdownItem.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                            )
                        }
                    }
                )
            }
        }
    }
}