package com.advancednotes.ui.screens.notes

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.domain.models.AdvancedFabMenuItem
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.NotesQuery
import com.advancednotes.domain.models.TopAppBarItem
import com.advancednotes.ui.components.ContentEmpty
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.notes.NoteItem
import com.advancednotes.ui.components.notes.filters.Filters
import com.advancednotes.ui.components.notes.filters.FiltersState
import com.advancednotes.ui.components.notes.filters.FiltersViewModel
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.ui.screens.notes.dialogs.DeleteNotePermanentlyDialog
import com.advancednotes.ui.screens.notes.dialogs.DuplicateNoteDialog
import com.advancednotes.ui.screens.notes.dialogs.EmptyTrashDialog
import com.advancednotes.ui.screens.notes.dialogs.NoteDialogState
import com.advancednotes.ui.screens.notes.dialogs.TrashNoteDialog
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    notesQuery: NotesQuery,
    mainActivityViewModel: MainActivityViewModel,
    notesViewModel: NotesViewModel = hiltViewModel(),
    filtersViewModel: FiltersViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val activity: Activity? = (context as? Activity)
    val density: Density = LocalDensity.current

    val notesState: NotesState by notesViewModel.notesState.collectAsState()
    val filtersState: FiltersState by filtersViewModel.filtersState.collectAsState()

    val lazyListState: LazyListState = rememberLazyListState()
    var filtersVisible: Boolean by remember { mutableStateOf(false) }

    val uncategorizedTagName: String = stringResource(id = R.string.without_tags)

    var duplicateNoteDialogState: NoteDialogState by remember {
        mutableStateOf(
            NoteDialogState()
        )
    }

    var trashNoteDialogState: NoteDialogState by remember {
        mutableStateOf(
            NoteDialogState()
        )
    }

    var deletePermanentlyDialogState: NoteDialogState by remember {
        mutableStateOf(
            NoteDialogState()
        )
    }

    var emptyTrashDialogOpened: Boolean by remember { mutableStateOf(false) }

    BackHandler {
        when (notesQuery) {
            NotesQuery.ALL -> {
                activity?.finish()
            }

            NotesQuery.ARCHIVED -> {
                navController.popBackStack()
            }

            NotesQuery.TRASHED -> {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(Unit) {
        mainScope.launch {
            when (notesQuery) {
                NotesQuery.ALL -> {
                    mainActivityViewModel.setCurrentRouteState(Screen.NotesScreen.route)
                    mainActivityViewModel.setTopAppBarState(
                        MyTopAppBarState(
                            label = context.getString(Screen.NotesScreen.labelResource),
                            navigationBack = false
                        )
                    )
                }

                NotesQuery.ARCHIVED -> {
                    mainActivityViewModel.setCurrentRouteState(Screen.ArchivedNotesScreen.route)
                    mainActivityViewModel.setTopAppBarState(
                        MyTopAppBarState(
                            label = context.getString(Screen.ArchivedNotesScreen.labelResource),
                            navigationBack = false
                        )
                    )
                }

                NotesQuery.TRASHED -> {
                    mainActivityViewModel.setCurrentRouteState(Screen.TrashedNotesScreen.route)
                    mainActivityViewModel.setTopAppBarState(
                        MyTopAppBarState(
                            label = context.getString(Screen.TrashedNotesScreen.labelResource),
                            navigationBack = false
                        )
                    )
                }
            }
        }

        notesViewModel.getNotes(
            notesQuery = notesQuery,
            uncategorizedTagName = uncategorizedTagName,
            filterQuery = filtersState.searchQuery,
            filterNoteTagsUUIDs = filtersState.selectedTagsUUIDs
        )
    }

    LaunchedEffect(notesState.isLoading) {
        if (!notesState.isLoading) {
            mainScope.launch {
                when (notesQuery) {
                    NotesQuery.ALL -> {
                        val advancedFabMenuVerticalItems: List<AdvancedFabMenuItem> =
                            listOf(
                                AdvancedFabMenuItem(
                                    icon = Icons.Default.Add,
                                    text = context.getString(R.string.add_new_note),
                                    onClick = {
                                        navController.myNavigate(
                                            route = Screen.SingleNoteScreen.route,
                                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                        )
                                    }
                                )
                            )

                        val advancedFabMenuHorizontalItems: List<AdvancedFabMenuItem> =
                            listOf(
                                AdvancedFabMenuItem(
                                    icon = Icons.Default.Tag,
                                    text = context.getString(R.string.screen_tags_label),
                                    onClick = {
                                        navController.myNavigate(
                                            route = Screen.TagsScreen.route,
                                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                        )
                                    }
                                )
                            )

                        mainActivityViewModel.setAdvancedFabMenuItems(
                            verticalItems = advancedFabMenuVerticalItems,
                            horizontalItems = advancedFabMenuHorizontalItems
                        )
                    }

                    NotesQuery.ARCHIVED -> {}

                    NotesQuery.TRASHED -> {
                        val topBarActions: List<TopAppBarItem> = listOf(
                            TopAppBarItem(
                                icon = Icons.Default.Delete,
                                text = context.getString(R.string.empty_trash_title),
                                onClick = {
                                    emptyTrashDialogOpened = true
                                }
                            )
                        )

                        mainActivityViewModel.setTopAppBarActions(topBarActions)
                    }
                }
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.canScrollBackward
        }.collect { canScrollBackward ->
            filtersVisible = !canScrollBackward
        }
    }

    LaunchedEffect(notesViewModel.refreshNotesChannel) {
        notesViewModel.refreshNotesChannel.receiveAsFlow().collectLatest {
            notesViewModel.getNotes(
                notesQuery = notesQuery,
                uncategorizedTagName = uncategorizedTagName,
                filterQuery = filtersState.searchQuery,
                filterNoteTagsUUIDs = filtersState.selectedTagsUUIDs
            )
        }
    }

    ScreenContainer(
        topPadding = false,
        bottomPadding = false
    ) {
        Column {
            AnimatedVisibility(
                visible = filtersVisible,
                modifier = Modifier.padding(top = CONTENT_PADDING_DEFAULT),
                enter = slideInVertically {
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Filters(
                    notesState = notesState,
                    onFilter = { searchQuery, usedTagsUUIDs ->
                        notesViewModel.getNotes(
                            notesQuery = notesQuery,
                            uncategorizedTagName = uncategorizedTagName, filterQuery = searchQuery,
                            filterNoteTagsUUIDs = usedTagsUUIDs
                        )
                    },
                    filtersViewModel = filtersViewModel
                )
            }

            if (notesState.notes.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = CONTENT_PADDING_DEFAULT),
                    verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
                ) {
                    items(notesState.notes) { note ->
                        NoteItem(
                            note = note,
                            contextMenuItemData = when (notesQuery) {
                                NotesQuery.ALL -> {
                                    listOf(
                                        ContextMenuItem(
                                            text = if (note.fixed) {
                                                stringResource(id = R.string.cd_unfixed)
                                            } else stringResource(id = R.string.cd_fixed),
                                            onClick = {
                                                notesViewModel.fixNote(note.uuid, note.fixed)
                                            },
                                            icon = if (!note.fixed) {
                                                Icons.Default.PushPin
                                            } else Icons.Outlined.PushPin
                                        ),
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.cd_archive),
                                            onClick = {
                                                notesViewModel.archiveNote(note.uuid)
                                            },
                                            icon = Icons.Default.Archive
                                        ),
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.duplicate),
                                            onClick = {
                                                duplicateNoteDialogState =
                                                    duplicateNoteDialogState.copy(
                                                        opened = true,
                                                        noteUUID = note.uuid
                                                    )
                                            },
                                            icon = Icons.Default.ContentCopy
                                        ),
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.delete),
                                            onClick = {
                                                trashNoteDialogState =
                                                    trashNoteDialogState.copy(
                                                        opened = true,
                                                        noteUUID = note.uuid
                                                    )
                                            },
                                            icon = Icons.Default.Delete
                                        )
                                    )
                                }

                                NotesQuery.ARCHIVED -> {
                                    listOf(
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.cd_unarchive),
                                            onClick = {
                                                notesViewModel.unarchiveNote(note.uuid)
                                            },
                                            icon = Icons.Outlined.Archive
                                        ),
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.delete),
                                            onClick = {
                                                trashNoteDialogState =
                                                    trashNoteDialogState.copy(
                                                        opened = true,
                                                        noteUUID = note.uuid
                                                    )
                                            },
                                            icon = Icons.Default.Delete
                                        )
                                    )
                                }

                                NotesQuery.TRASHED -> {
                                    listOf(
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.restore),
                                            onClick = {
                                                notesViewModel.restoreNote(note.uuid)
                                            },
                                            icon = Icons.Default.Restore
                                        ),
                                        ContextMenuItem(
                                            text = stringResource(id = R.string.delete_permanently),
                                            onClick = {
                                                deletePermanentlyDialogState =
                                                    deletePermanentlyDialogState.copy(
                                                        opened = true,
                                                        noteUUID = note.uuid
                                                    )
                                            },
                                            icon = Icons.Default.Delete
                                        )
                                    )
                                }
                            },
                            onClick = {
                                when (notesQuery) {
                                    NotesQuery.ALL -> {
                                        navController.myNavigate(
                                            route = Screen.SingleNoteScreen.route + "?noteUUID=${note.uuid}",
                                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                        )
                                    }

                                    NotesQuery.ARCHIVED -> {
                                        navController.myNavigate(
                                            route = Screen.SingleNoteScreen.route + "?noteUUID=${note.uuid}",
                                            afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                        )
                                    }

                                    NotesQuery.TRASHED -> {}
                                }
                            }
                        )
                    }
                }
            } else {
                if (!notesState.isLoading) {
                    when (notesQuery) {
                        NotesQuery.ALL -> {
                            ContentEmpty(text = stringResource(id = R.string.content_empty_notes))
                        }

                        NotesQuery.ARCHIVED -> {
                            ContentEmpty(text = stringResource(id = R.string.content_empty_archived_notes))
                        }

                        NotesQuery.TRASHED -> {
                            ContentEmpty(text = stringResource(id = R.string.content_empty_trash))
                        }
                    }
                }
            }
        }
    }

    DuplicateNoteDialog(
        duplicateNoteDialogState = duplicateNoteDialogState,
        onConfirm = {
            notesViewModel.duplicateNote(context, duplicateNoteDialogState.noteUUID)
        },
        onCloseDialog = {
            duplicateNoteDialogState = duplicateNoteDialogState.copy(opened = false)
        }
    )

    TrashNoteDialog(
        trashNoteDialogState = trashNoteDialogState,
        onConfirm = {
            notesViewModel.trashNote(trashNoteDialogState.noteUUID)
        },
        onCloseDialog = {
            trashNoteDialogState = trashNoteDialogState.copy(opened = false)
        }
    )

    DeleteNotePermanentlyDialog(
        deletePermanentlyDialogState = deletePermanentlyDialogState,
        onConfirm = {
            notesViewModel.deleteNotePermanently(deletePermanentlyDialogState.noteUUID)
        },
        onCloseDialog = {
            deletePermanentlyDialogState = deletePermanentlyDialogState.copy(opened = false)
        }
    )

    EmptyTrashDialog(
        opened = emptyTrashDialogOpened,
        onConfirm = {
            notesViewModel.deleteNotesTrashed()
        },
        onCloseDialog = {
            emptyTrashDialogOpened = false
        }
    )
}