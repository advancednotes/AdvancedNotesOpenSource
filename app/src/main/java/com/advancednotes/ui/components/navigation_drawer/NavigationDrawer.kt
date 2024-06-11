package com.advancednotes.ui.components.navigation_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.advancednotes.R
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.domain.models.NavigationDrawerItem
import com.advancednotes.ui.components.advanced_fab_menu.AdvancedFabMenu
import com.advancednotes.ui.components.advanced_fab_menu.AdvancedFabMenuState
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.components.top_app_bar.MyTopAppBar
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyNavigationDrawer(
    navController: NavHostController,
    mainActivityViewModel: MainActivityViewModel,
    content: @Composable () -> Unit
) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerScope: CoroutineScope = rememberCoroutineScope()
    val currentRouteState: State<String> =
        mainActivityViewModel.currentScreenRouteState.collectAsState()
    val notesCountState by remember { MainActivityViewModel.notesCountState }.collectAsState()

    val items: List<NavigationDrawerItem> = listOf(
        NavigationDrawerItem(
            route = Screen.NotesScreen.route,
            title = stringResource(id = R.string.screen_notes_label),
            contentDescription = stringResource(id = R.string.screen_notes_label),
            icon = Icons.AutoMirrored.Filled.Notes,
            count = notesCountState.notesCount
        ),
        NavigationDrawerItem(
            route = Screen.ArchivedNotesScreen.route,
            title = stringResource(id = R.string.screen_archived_notes_label),
            contentDescription = stringResource(id = R.string.screen_archived_notes_label),
            icon = Icons.Default.Archive,
            count = notesCountState.notesArchivedCount
        ),
        NavigationDrawerItem(
            route = Screen.TrashedNotesScreen.route,
            title = stringResource(id = R.string.screen_trashed_notes_label),
            contentDescription = stringResource(id = R.string.screen_trashed_notes_label),
            icon = Icons.Default.Delete,
            count = notesCountState.notesTrashedCount
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column {
                ModalDrawerSheet {
                    DrawerHeader(
                        navController = navController,
                        drawerState = drawerState,
                        drawerScope = drawerScope,
                        mainActivityViewModel = mainActivityViewModel
                    )

                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.contentDescription,
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            },
                            badge = if (item.count != null) {
                                {
                                    MyText(
                                        text = item.count.toString(),
                                        textStyle = MaterialTheme.typography.labelSmall
                                    )
                                }
                            } else null,
                            label = {
                                MyText(
                                    text = item.title,
                                    textStyle = MaterialTheme.typography.titleSmall
                                )
                            },
                            selected = item.route == currentRouteState.value,
                            onClick = {
                                drawerScope.launch { drawerState.close() }
                                navController.myNavigate(
                                    route = item.route,
                                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
        content = {
            val topAppBarState: State<MyTopAppBarState> =
                mainActivityViewModel.topAppBarState.collectAsState()
            val advancedFabMenuState: State<AdvancedFabMenuState> =
                mainActivityViewModel.advancedFabMenuState.collectAsState()
            val snackbarHostState: State<SnackbarHostState> =
                mainActivityViewModel.snackbarHostState.collectAsState()

            Scaffold(
                topBar = {
                    MyTopAppBar(
                        drawerState,
                        drawerScope,
                        topAppBarState.value
                    )
                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState.value) { data ->
                        val isError: Boolean =
                            (data.visuals as? MySnackbarVisuals)?.isError ?: false

                        val buttonColors: ButtonColors = if (isError) {
                            ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        } else {
                            ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            )
                        }

                        Snackbar(
                            modifier = Modifier
                                .padding(12.dp),
                            action = {
                                if (data.visuals.withDismissAction) {
                                    MyTextButton(
                                        text = data.visuals.actionLabel ?: "",
                                        onClick = {
                                            data.performAction()
                                        },
                                        colors = buttonColors
                                    )
                                }
                            },
                            shape = MaterialTheme.shapes.small,
                            /*containerColor = if (isError) {
                                MaterialTheme.colorScheme.errorContainer
                            } else MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = if (isError) {
                                MaterialTheme.colorScheme.onErrorContainer
                            } else MaterialTheme.colorScheme.onTertiaryContainer,*/
                        ) {
                            MyText(data.visuals.message)
                        }
                    }
                },
                content = { scaffoldPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding)
                    ) {
                        content()
                        AdvancedFabMenu(advancedFabMenuState.value)
                    }
                }
            )
        }
    )
}