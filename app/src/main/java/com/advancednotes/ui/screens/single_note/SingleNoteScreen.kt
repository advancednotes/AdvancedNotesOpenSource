package com.advancednotes.ui.screens.single_note

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.models.AdvancedFabMenuItem
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.domain.models.TopAppBarItem
import com.advancednotes.domain.models.isNotEmpty
import com.advancednotes.ui.components.MyAccordion
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.single_note.NoteGallery
import com.advancednotes.ui.components.single_note.NoteLocation
import com.advancednotes.ui.components.single_note.SingleNoteTagsChipGroup
import com.advancednotes.ui.components.single_note.advanced_content.AdvancedContent
import com.advancednotes.ui.components.single_note.records.NoteRecords
import com.advancednotes.ui.components.textfields.EmptyTextField
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.ui.screens.single_note.dialogs.DeleteLocationDialog
import com.advancednotes.ui.screens.single_note.dialogs.TagsDialog
import com.advancednotes.ui.screens.single_note.dialogs.TrashNoteDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.CameraPermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.ExactAlarmsPermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.LocationPermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.NotificationsPermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.RecordPermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.permissions.StoragePermissionDialog
import com.advancednotes.ui.screens.single_note.dialogs.reminder.ReminderDialog
import com.advancednotes.utils.permissions.allPermissionsGranted
import com.advancednotes.utils.permissions.isGranted
import com.advancednotes.utils.services.RecordAudioService.Companion.recordAdded
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SingleNoteScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    noteUUID: String = EMPTY_UUID,
    mainActivityViewModel: MainActivityViewModel,
    singleNoteViewModel: SingleNoteViewModel = hiltViewModel(),
    recordsViewModel: RecordsViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val primaryColor: Color = MaterialTheme.colorScheme.primary

    val singleNoteState: SingleNoteState by singleNoteViewModel.singleNoteState.collectAsState()

    val scrollState: ScrollState = rememberScrollState()

    var noteName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    var showNoteNameEmptyError: Boolean by remember { mutableStateOf(false) }

    var reminderDialogOpened: Boolean by remember { mutableStateOf(false) }
    var trashNoteDialogOpened: Boolean by remember { mutableStateOf(false) }
    var tagsDialogOpened: Boolean by remember { mutableStateOf(false) }
    var deleteLocationDialogOpened: Boolean by remember { mutableStateOf(false) }

    var notificationsPermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }
    var exactAlarmsPermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }
    var locationPermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }
    var cameraPermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }
    var recordPermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }
    var mediaAudioStoragePermissionRationaleDialogOpened: Boolean by remember { mutableStateOf(false) }

    val notificationsPermissionState: PermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    val locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val cameraPermissionState: PermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    val recordPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECORD_AUDIO
            )
        } else {
            listOf(
                Manifest.permission.RECORD_AUDIO
            )
        }
    )

    val mediaAudioStoragePermissionState: MultiplePermissionsState =
        rememberMultiplePermissionsState(
            permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            } else {
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        )

    var tempFileUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                singleNoteViewModel.onPictureCaptured(context, tempFileUri)
            }
        }
    )

    fun onBack() {
        if (singleNoteViewModel.recordServiceIsActive()) {
            singleNoteViewModel.showSnackBarRecordingAudio()
            return
        }

        if (singleNoteViewModel.isNewNoteWithEmptyData()) {
            singleNoteViewModel.exit()
            return
        }

        if (!singleNoteViewModel.validateData()) {
            if (noteName.text.isEmpty()) {
                showNoteNameEmptyError = true
                singleNoteViewModel.showSnackbarDiscardChangesChannel()
            }
            return
        }

        if (recordsViewModel.isPlaying()) {
            recordsViewModel.stopRecord(
                onStop = {
                    scope.launch {
                        recordsViewModel.setRecordPlayingIdentifier("")
                    }
                }
            )
        }

        mainActivityViewModel.clearAdvancedFabMenuState()
        singleNoteViewModel.saveNoteAndExit()
    }

    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.SingleNoteScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.SingleNoteScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        onBack()
                    }
                )
            )
        }

        if (singleNoteState.isLoading) {
            singleNoteViewModel.getNote(noteUUID)
            singleNoteViewModel.getStickyNote(context, noteUUID)
        }

        singleNoteViewModel.getGalleryImages(noteUUID)
        singleNoteViewModel.getRecords(noteUUID)

        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            scope.launch {
                recordAdded.collectLatest {
                    singleNoteViewModel.setRecordingAudio(
                        active = false,
                        context = context
                    )
                    singleNoteViewModel.getRecords(noteUUID)
                }
            }
        }
    }

    LaunchedEffect(
        singleNoteState.isLoading,
        singleNoteState.note.fixed,
        singleNoteState.note.reminder,
        singleNoteState.note.archived,
        singleNoteState.gettingLocation,
        singleNoteState.recordingAudio,
        singleNoteState.isStickyNote
    ) {
        if (!singleNoteState.isLoading) {
            mainScope.launch {
                val topBarActions: MutableList<TopAppBarItem> = mutableListOf()

                topBarActions.add(
                    TopAppBarItem(
                        icon = if (singleNoteState.note.fixed) {
                            Icons.Default.PushPin
                        } else Icons.Outlined.PushPin,
                        text = if (singleNoteState.note.fixed) {
                            context.getString(R.string.cd_unfixed)
                        } else context.getString(R.string.cd_fixed),
                        onClick = {
                            singleNoteViewModel.changeNoteFixed()
                        }
                    )
                )
                topBarActions.add(
                    TopAppBarItem(
                        icon = if (singleNoteState.note.reminder != null) {
                            Icons.Default.Notifications
                        } else Icons.Outlined.NotificationAdd,
                        text = context.getString(R.string.reminder),
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val notificationsPermissionsGranted: Boolean =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        notificationsPermissionState.isGranted()
                                    } else true

                                if (
                                    notificationsPermissionsGranted &&
                                    alarmManager.canScheduleExactAlarms()
                                ) {
                                    reminderDialogOpened = true
                                }

                                if (!notificationsPermissionsGranted) {
                                    notificationsPermissionRationaleDialogOpened = true
                                }

                                if (!alarmManager.canScheduleExactAlarms()) {
                                    exactAlarmsPermissionRationaleDialogOpened = true
                                }
                            } else {
                                reminderDialogOpened = true
                            }
                        }
                    )
                )

                if (noteUUID != EMPTY_UUID) {
                    topBarActions.add(
                        TopAppBarItem(
                            icon = if (singleNoteState.note.archived) {
                                Icons.Default.Archive
                            } else Icons.Outlined.Archive,
                            text = if (singleNoteState.note.archived) {
                                context.getString(R.string.cd_unarchive)
                            } else context.getString(R.string.cd_archive),
                            onClick = {
                                singleNoteViewModel.changeNoteArchived()
                            }
                        )
                    )
                }

                mainActivityViewModel.setTopAppBarActions(topBarActions)

                val topBarMenuActions: MutableList<TopAppBarItem> = mutableListOf()

                if (noteUUID != EMPTY_UUID) {
                    topBarMenuActions.add(
                        TopAppBarItem(
                            icon = Icons.AutoMirrored.Filled.StickyNote2,
                            text = if (singleNoteState.isStickyNote) {
                                context.getString(R.string.no_sticky_note)
                            } else context.getString(R.string.sticky_note),
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    when {
                                        notificationsPermissionState.isGranted() -> {
                                            singleNoteViewModel.changeStickyNote()
                                        }

                                        else -> {
                                            notificationsPermissionRationaleDialogOpened = true
                                        }
                                    }
                                } else {
                                    singleNoteViewModel.changeStickyNote()
                                }
                            }
                        )
                    )

                    topBarMenuActions.add(
                        TopAppBarItem(
                            icon = Icons.Default.Delete,
                            text = context.getString(R.string.delete),
                            onClick = {
                                trashNoteDialogOpened = true
                            }
                        )
                    )
                }

                mainActivityViewModel.setTopAppBarMenuActions(topBarMenuActions)

                val advancedFabMenuVerticalItems: MutableList<AdvancedFabMenuItem> = mutableListOf()

                advancedFabMenuVerticalItems.add(
                    AdvancedFabMenuItem(
                        icon = Icons.Default.Tag,
                        text = context.getString(R.string.screen_tags_label),
                        onClick = {
                            tagsDialogOpened = true
                        }
                    )
                )

                advancedFabMenuVerticalItems.add(
                    AdvancedFabMenuItem(
                        icon = if (singleNoteState.gettingLocation) {
                            Icons.Default.MyLocation
                        } else Icons.Default.LocationOn,
                        text = context.getString(R.string.note_location),
                        onClick = {
                            when {
                                locationPermissionState.allPermissionsGranted() -> {
                                    if (!singleNoteState.gettingLocation) {
                                        singleNoteViewModel.changeNoteLocation()
                                    }
                                }

                                else -> {
                                    locationPermissionRationaleDialogOpened = true
                                }
                            }
                        },
                        iconTint = if (singleNoteState.gettingLocation) {
                            primaryColor
                        } else null
                    )
                )

                advancedFabMenuVerticalItems.add(
                    AdvancedFabMenuItem(
                        icon = Icons.Default.Camera,
                        text = context.getString(R.string.note_camera),
                        onClick = {
                            when {
                                cameraPermissionState.isGranted() -> {
                                    tempFileUri = singleNoteViewModel.createTempPictureFile(context)
                                    cameraLauncher.launch(tempFileUri!!)
                                }

                                else -> {
                                    cameraPermissionRationaleDialogOpened = true
                                }
                            }
                        }
                    )
                )

                advancedFabMenuVerticalItems.add(
                    AdvancedFabMenuItem(
                        icon = if (singleNoteState.recordingAudio) {
                            Icons.Default.StopCircle
                        } else Icons.Default.Mic,
                        text = context.getString(R.string.note_record),
                        onClick = {
                            when {
                                recordPermissionState.allPermissionsGranted() -> {
                                    if (singleNoteViewModel.notificationsAreAvailable()) {
                                        if (singleNoteState.recordingAudio) {
                                            singleNoteViewModel.setRecordingAudio(
                                                active = false,
                                                context = context
                                            )
                                        } else {
                                            singleNoteViewModel.setRecordingAudio(
                                                active = true,
                                                context = context
                                            )
                                        }

                                    } else {
                                        recordPermissionRationaleDialogOpened = true
                                    }
                                }

                                else -> {
                                    recordPermissionRationaleDialogOpened = true
                                }
                            }
                        },
                        iconTint = if (singleNoteState.recordingAudio) {
                            primaryColor
                        } else null
                    )
                )

                mainActivityViewModel.setAdvancedFabMenuItems(
                    verticalItems = advancedFabMenuVerticalItems,
                    setNotExpanded = false
                )
            }
        }
    }

    LaunchedEffect(singleNoteState.isLoading) {
        if (!singleNoteState.isLoading) {
            noteName = TextFieldValue(text = singleNoteState.note.name)
        }
    }

    LaunchedEffect(singleNoteViewModel.snackbarRecordingAudioChannel) {
        singleNoteViewModel.snackbarRecordingAudioChannel.receiveAsFlow().collectLatest {
            val message: String = context.getString(R.string.recording_audio_message)

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = false,
                    actionLabel = context.getString(R.string.cd_stop_record)
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    singleNoteViewModel.setRecordingAudio(
                        active = false,
                        context = context
                    )
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(singleNoteViewModel.snackbarUndoLocationChannel) {
        singleNoteViewModel.snackbarUndoLocationChannel.receiveAsFlow().collectLatest {
            val message: String = context.getString(R.string.location_updated)

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = true,
                    actionLabel = context.getString(R.string.undo_button)
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    singleNoteViewModel.undoLocation()
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(singleNoteViewModel.snackbarDiscardChangesChannel) {
        singleNoteViewModel.snackbarDiscardChangesChannel.receiveAsFlow().collectLatest {
            val message: String = context.getString(R.string.error_note_name_empty)

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = true,
                    actionLabel = context.getString(R.string.discard_changes)
                )
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    singleNoteViewModel.exit()
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(singleNoteViewModel.refreshRecordsChannel) {
        singleNoteViewModel.refreshRecordsChannel.receiveAsFlow().collectLatest {
            singleNoteViewModel.getRecords(noteUUID)
        }
    }

    LaunchedEffect(singleNoteViewModel.exitChannel) {
        singleNoteViewModel.exitChannel.receiveAsFlow().collectLatest {
            navController.popBackStack()
        }
    }

    ScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
        ) {
            Column {
                EmptyTextField(
                    value = noteName,
                    onValueChange = {
                        noteName = it
                        singleNoteViewModel.changeNoteName(it.text)
                        if (it.text.isNotEmpty()) showNoteNameEmptyError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    textFieldModifier = Modifier
                        .padding(all = 5.dp),
                    textStyle = MaterialTheme.typography.titleMedium,
                    placeholder = stringResource(id = R.string.note_name),
                    supportingText = if (showNoteNameEmptyError && !singleNoteState.isLoading) {
                        context.getString(R.string.error_note_name_empty)
                    } else "",
                    isError = showNoteNameEmptyError && !singleNoteState.isLoading
                )
            }

            AdvancedContent(
                isLoading = singleNoteState.isLoading,
                advancedContent = singleNoteState.note.advancedContent,
                onUpdateAdvancedContent = { advancedContent ->
                    singleNoteViewModel.changeAdvancedContent(advancedContent)
                }
            )

            if (singleNoteState.note.tagsUUIDs.isNotEmpty()) {
                MyAccordion(
                    title = stringResource(id = R.string.screen_tags_label)
                ) {
                    SingleNoteTagsChipGroup(singleNoteState.tags, singleNoteState.note.tagsUUIDs)
                }
            }

            if (singleNoteState.note.location?.isNotEmpty() == true) {
                MyAccordion(
                    title = stringResource(id = R.string.note_location)
                ) {
                    NoteLocation(
                        myLocation = singleNoteState.note.location!!,
                        onDeleteLocation = { deleteLocationDialogOpened = true }
                    )
                }
            }

            if (singleNoteState.noteImages.isNotEmpty()) {
                MyAccordion(
                    title = stringResource(id = R.string.note_gallery)
                ) {
                    NoteGallery(
                        navController = navController,
                        noteImages = singleNoteState.noteImages,
                        mainActivityViewModel = mainActivityViewModel,
                        singleNoteViewModel = singleNoteViewModel
                    )
                }
            }

            if (singleNoteState.noteRecords.isNotEmpty()) {
                MyAccordion(
                    title = stringResource(id = R.string.note_records)
                ) {
                    NoteRecords(
                        context = context,
                        noteRecords = singleNoteState.noteRecords,
                        storagePermissionGranted = mediaAudioStoragePermissionState.allPermissionsGranted(),
                        openStoragePermissionRationaleDialog = {
                            mediaAudioStoragePermissionRationaleDialogOpened = true
                        },
                        singleNoteViewModel = singleNoteViewModel,
                        recordsViewModel = recordsViewModel
                    )
                }
            }
        }
    }

    ReminderDialog(
        opened = reminderDialogOpened,
        reminder = singleNoteState.note.reminder,
        onChangeReminder = { reminder ->
            singleNoteViewModel.changeReminder(reminder)
        },
        onCloseDialog = {
            reminderDialogOpened = false
        }
    )

    TrashNoteDialog(
        opened = trashNoteDialogOpened,
        onTrashNote = {
            singleNoteViewModel.trashNote(noteUUID = noteUUID)
        },
        onCloseDialog = {
            trashNoteDialogOpened = false
        }
    )

    TagsDialog(
        opened = tagsDialogOpened,
        tags = singleNoteState.tags,
        noteTagsUUIDs = singleNoteState.note.tagsUUIDs,
        onSelectTag = { tagUUID ->
            singleNoteViewModel.selectTag(tagUUID)
        },
        onCloseDialog = {
            tagsDialogOpened = false
        }
    )

    DeleteLocationDialog(
        opened = deleteLocationDialogOpened,
        onDeleteLocation = {
            singleNoteViewModel.changeNoteLocation(delete = true)
        },
        onCloseDialog = {
            deleteLocationDialogOpened = false
        }
    )

    /**
     * Permissions dialogs
     * */
    NotificationsPermissionDialog(
        opened = notificationsPermissionRationaleDialogOpened,
        onConfirm = {
            notificationsPermissionState.launchPermissionRequest()
        },
        onCloseDialog = {
            notificationsPermissionRationaleDialogOpened = false
        }
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ExactAlarmsPermissionDialog(
            opened = exactAlarmsPermissionRationaleDialogOpened,
            onConfirm = {
                context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            },
            onCloseDialog = {
                exactAlarmsPermissionRationaleDialogOpened = false
            }
        )
    }

    LocationPermissionDialog(
        opened = locationPermissionRationaleDialogOpened,
        onConfirm = {
            locationPermissionState.launchMultiplePermissionRequest()
        },
        onCloseDialog = {
            locationPermissionRationaleDialogOpened = false
        }
    )

    CameraPermissionDialog(
        opened = cameraPermissionRationaleDialogOpened,
        onConfirm = {
            cameraPermissionState.launchPermissionRequest()
        },
        onCloseDialog = {
            cameraPermissionRationaleDialogOpened = false
        }
    )

    RecordPermissionDialog(
        opened = recordPermissionRationaleDialogOpened,
        onConfirm = {
            recordPermissionState.launchMultiplePermissionRequest()
        },
        onCloseDialog = {
            recordPermissionRationaleDialogOpened = false
        }
    )

    StoragePermissionDialog(
        opened = mediaAudioStoragePermissionRationaleDialogOpened,
        onConfirm = {
            mediaAudioStoragePermissionState.launchMultiplePermissionRequest()
        },
        onCloseDialog = {
            mediaAudioStoragePermissionRationaleDialogOpened = false
        }
    )
}