package com.advancednotes.ui.screens.settings

import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.common.UIModeState.uiState
import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.UiMode
import com.advancednotes.ui.components.ColumnWithLabel
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.Spinner
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    mainActivityViewModel: MainActivityViewModel,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val settingsState: SettingsState by settingsViewModel.settingsState.collectAsState()

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.SettingsScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.SettingsScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )
        }

        settingsViewModel.getAutoDeleteTrashMode(context)
    }

    ScreenContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
        ) {
            ColumnWithLabel(
                label = stringResource(id = R.string.ui_mode)
            ) {
                Spinner(
                    items = listOf(
                        ContextMenuItem(
                            text = stringResource(id = R.string.ui_mode_device),
                            onClick = {
                                settingsViewModel.setUiMode(context, UiMode.DEVICE)
                            }
                        ),
                        ContextMenuItem(
                            text = stringResource(id = R.string.ui_mode_day),
                            onClick = {
                                settingsViewModel.setUiMode(context, UiMode.DAY)
                            }
                        ),
                        ContextMenuItem(
                            text = stringResource(id = R.string.ui_mode_night),
                            onClick = {
                                settingsViewModel.setUiMode(context, UiMode.NIGHT)
                            }
                        )
                    ),
                    selectedItemText = when (uiState.value.uiMode) {
                        UiMode.DEVICE -> stringResource(id = R.string.ui_mode_device)
                        UiMode.DAY -> stringResource(id = R.string.ui_mode_day)
                        UiMode.NIGHT -> stringResource(id = R.string.ui_mode_night)
                    }
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ColumnWithLabel(
                    label = stringResource(id = R.string.use_dynamic_colors)
                ) {
                    Switch(
                        checked = uiState.value.useDynamicColors,
                        onCheckedChange = { checked ->
                            settingsViewModel.setUseDynamicColors(context, checked)
                        }
                    )
                }
            }

            ColumnWithLabel(
                label = stringResource(id = R.string.auto_delete_trash_mode)
            ) {
                Spinner(
                    items = listOf(
                        ContextMenuItem(
                            text = stringResource(id = R.string.auto_delete_trash_mode_none),
                            onClick = {
                                settingsViewModel.setAutoDeleteTrashMode(
                                    context,
                                    AutoDeleteTrashMode.NEVER
                                )
                            }
                        ),
                        ContextMenuItem(
                            text = stringResource(id = R.string.auto_delete_trash_mode_monthly),
                            onClick = {
                                settingsViewModel.setAutoDeleteTrashMode(
                                    context,
                                    AutoDeleteTrashMode.MONTHLY
                                )
                            }
                        ),
                        ContextMenuItem(
                            text = stringResource(id = R.string.auto_delete_trash_mode_six_monthly),
                            onClick = {
                                settingsViewModel.setAutoDeleteTrashMode(
                                    context,
                                    AutoDeleteTrashMode.SIX_MONTHLY
                                )
                            }
                        )
                    ),
                    selectedItemText = when (settingsState.autoDeleteTrashMode) {
                        AutoDeleteTrashMode.NEVER -> stringResource(id = R.string.auto_delete_trash_mode_none)
                        AutoDeleteTrashMode.MONTHLY -> stringResource(id = R.string.auto_delete_trash_mode_monthly)
                        AutoDeleteTrashMode.SIX_MONTHLY -> stringResource(id = R.string.auto_delete_trash_mode_six_monthly)
                    }
                )
            }
        }
    }
}