package com.advancednotes.ui.components.navigation_drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.AUTH_ENABLED
import com.advancednotes.ui.components.buttons.MyIconButton
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerHeader(
    navController: NavHostController,
    drawerState: DrawerState,
    drawerScope: CoroutineScope,
    mainActivityViewModel: MainActivityViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(110.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            MyIconButton(
                icon = Icons.Default.Settings,
                onClick = {
                    drawerScope.launch { drawerState.close() }
                    navController.myNavigate(
                        route = Screen.SettingsScreen.route,
                        afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                    )
                },
                contentDescription = stringResource(id = R.string.screen_settings_label),
                iconSize = 28.dp
            )
        }

        if (AUTH_ENABLED) {
            LoginDropdown(
                navController = navController,
                drawerState = drawerState,
                drawerScope = drawerScope,
                mainActivityViewModel = mainActivityViewModel,
                modifier = Modifier
                    .padding(top = 7.dp)
                    .align(Alignment.End)
            )
        }
    }
}