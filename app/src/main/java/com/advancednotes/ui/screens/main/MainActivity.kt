package com.advancednotes.ui.screens.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.models.NotesQuery
import com.advancednotes.ui.components.Synchronizing
import com.advancednotes.ui.components.navigation_drawer.MyNavigationDrawer
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.authentication.login.LoginScreen
import com.advancednotes.ui.screens.authentication.register.RegisterScreen
import com.advancednotes.ui.screens.authentication.user.UserScreen
import com.advancednotes.ui.screens.note_gallery.NoteGalleryScreen
import com.advancednotes.ui.screens.notes.NotesScreen
import com.advancednotes.ui.screens.settings.SettingsScreen
import com.advancednotes.ui.screens.single_note.SingleNoteScreen
import com.advancednotes.ui.screens.single_tag.SingleTagScreen
import com.advancednotes.ui.screens.tags.TagsScreen
import com.advancednotes.ui.theme.AdvancedNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AdvancedNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context: Context = LocalContext.current
                    val mainScope: CoroutineScope = rememberCoroutineScope()
                    val navController: NavHostController = rememberNavController()

                    val synchronizeState = mainActivityViewModel.synchronizeState.collectAsState()

                    LaunchedEffect(Unit) {
                        mainActivityViewModel.syncronize(context)
                        mainActivityViewModel.initUser()
                        mainActivityViewModel.initNotesCount()
                    }

                    if (!synchronizeState.value.isSynchronizing) {
                        MyNavigationDrawer(
                            navController = navController,
                            mainActivityViewModel = mainActivityViewModel
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.NotesScreen.route
                            ) {
                                composable(
                                    route = Screen.LoginScreen.route
                                ) { backStackEntry ->
                                    LoginScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.RegisterScreen.route
                                ) { backStackEntry ->
                                    RegisterScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.SettingsScreen.route
                                ) { backStackEntry ->
                                    SettingsScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.UserScreen.route
                                ) { backStackEntry ->
                                    UserScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.NotesScreen.route
                                ) { backStackEntry ->
                                    NotesScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        notesQuery = NotesQuery.ALL,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.ArchivedNotesScreen.route
                                ) { backStackEntry ->
                                    NotesScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        notesQuery = NotesQuery.ARCHIVED,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.TrashedNotesScreen.route
                                ) { backStackEntry ->
                                    NotesScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        notesQuery = NotesQuery.TRASHED,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.SingleNoteScreen.route + "?noteUUID={noteUUID}",
                                    arguments = listOf(
                                        navArgument("noteUUID") {
                                            type = NavType.StringType
                                            nullable = false
                                            defaultValue = EMPTY_UUID
                                        }
                                    ),
                                    deepLinks = listOf(navDeepLink {
                                        uriPattern =
                                            "advancednotes://${Screen.SingleNoteScreen.route}?noteUUID={noteUUID}"
                                    })
                                ) { backStackEntry ->
                                    val noteUUID: String =
                                        backStackEntry.arguments!!.getString("noteUUID")
                                            ?: EMPTY_UUID

                                    SingleNoteScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        noteUUID = noteUUID,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.NoteGalleryScreen.route + "/{noteFileUUID}",
                                    arguments = listOf(
                                        navArgument("noteFileUUID") {
                                            type = NavType.StringType
                                            nullable = false
                                            defaultValue = EMPTY_UUID
                                        }
                                    )
                                ) { backStackEntry ->
                                    val noteFileUUID: String =
                                        backStackEntry.arguments!!.getString("noteFileUUID")
                                            ?: EMPTY_UUID

                                    NoteGalleryScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        noteFileUUID = noteFileUUID,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.TagsScreen.route
                                ) { backStackEntry ->
                                    TagsScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }

                                composable(
                                    route = Screen.SingleTagScreen.route + "?tagUUID={tagUUID}",
                                    arguments = listOf(
                                        navArgument("tagUUID") {
                                            type = NavType.StringType
                                            nullable = false
                                            defaultValue = EMPTY_UUID
                                        }
                                    )
                                ) { backStackEntry ->
                                    val tagUUID: String =
                                        backStackEntry.arguments!!.getString("tagUUID")
                                            ?: EMPTY_UUID

                                    SingleTagScreen(
                                        mainScope = mainScope,
                                        navController = navController,
                                        tagUUID = tagUUID,
                                        mainActivityViewModel = mainActivityViewModel
                                    )
                                }
                            }
                        }
                    } else {
                        Synchronizing()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mainActivityViewModel.startSetRemindersService(this)
        mainActivityViewModel.launchPeriodicWorkers()
    }
}