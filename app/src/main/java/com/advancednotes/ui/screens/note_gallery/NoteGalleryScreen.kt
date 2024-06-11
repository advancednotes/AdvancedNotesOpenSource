package com.advancednotes.ui.screens.note_gallery

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.models.AdvancedFabMenuItem
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.ui.screens.note_gallery.dialogs.DeleteImageDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.StoragePermissionDialog
import com.advancednotes.utils.permissions.allPermissionsGranted
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoteGalleryScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    noteFileUUID: String = EMPTY_UUID,
    mainActivityViewModel: MainActivityViewModel,
    noteGalleryViewModel: NoteGalleryViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val noteGalleryState: NoteGalleryState by noteGalleryViewModel.noteGalleryState.collectAsState()

    var deleteImageDialogOpened: Boolean by remember { mutableStateOf(false) }
    var mediaImagesStoragePermissionRationaleDialogOpened: Boolean by remember {
        mutableStateOf(
            false
        )
    }

    val mediaImagesStoragePermissionState: MultiplePermissionsState =
        rememberMultiplePermissionsState(
            permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        )

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.NoteGalleryScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.NoteGalleryScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )

            val advancedFabMenuVerticalItems: List<AdvancedFabMenuItem> =
                listOf(
                    AdvancedFabMenuItem(
                        icon = Icons.Default.Download,
                        text = context.getString(R.string.download),
                        onClick = {
                            when {
                                mediaImagesStoragePermissionState.allPermissionsGranted() -> {
                                    noteGalleryViewModel.downloadImage(
                                        context,
                                        noteGalleryState.noteFile
                                    )
                                }

                                else -> {
                                    mediaImagesStoragePermissionRationaleDialogOpened = true
                                }
                            }
                        }
                    ),
                    AdvancedFabMenuItem(
                        icon = Icons.Default.Delete,
                        text = context.getString(R.string.delete),
                        onClick = {
                            deleteImageDialogOpened = true
                        }
                    )
                )

            mainActivityViewModel.setAdvancedFabMenuItems(verticalItems = advancedFabMenuVerticalItems)
        }

        noteGalleryViewModel.getNoteFile(noteFileUUID)
    }

    LaunchedEffect(noteGalleryViewModel.exitChannel) {
        noteGalleryViewModel.exitChannel.receiveAsFlow().collectLatest {
            navController.popBackStack()
        }
    }

    ScreenContainer(
        startPadding = false,
        topPadding = false,
        endPadding = false,
        bottomPadding = false
    ) {
        noteGalleryViewModel.getPathFromNoteFile(noteGalleryState.noteFile)?.let {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(it)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.align(Alignment.Center),
                placeholder = null,
                contentDescription = null
            )
        }
    }

    DeleteImageDialog(
        deleteImageDialogOpened = deleteImageDialogOpened,
        onConfirm = {
            noteGalleryViewModel.deleteGalleryImage()
        },
        onCloseDialog = {
            deleteImageDialogOpened = false
        }
    )

    StoragePermissionDialog(
        opened = mediaImagesStoragePermissionRationaleDialogOpened,
        onConfirm = {
            mediaImagesStoragePermissionState.launchMultiplePermissionRequest()
        },
        onCloseDialog = {
            mediaImagesStoragePermissionRationaleDialogOpened = false
        }
    )
}