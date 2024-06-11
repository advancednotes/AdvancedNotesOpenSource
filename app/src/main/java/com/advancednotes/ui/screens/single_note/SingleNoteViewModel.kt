package com.advancednotes.ui.screens.single_note

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.common.Constants
import com.advancednotes.common.Constants.Companion.ACTION_START_SERVICE
import com.advancednotes.common.Constants.Companion.ACTION_STOP_SERVICE
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.datastore.getStickyNoteNotificationByNoteUUID
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.models.NoteReminder
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.models.isEmpty
import com.advancednotes.domain.usecases.LocationUseCase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import com.advancednotes.utils.date.DateHelper.getCurrentDateForFileName
import com.advancednotes.utils.files.FileHelper.getCacheFilesDirectory
import com.advancednotes.utils.notifications.NoteRecordAudioNotificationHelper
import com.advancednotes.utils.notifications.StickyNoteNotificationHelper
import com.advancednotes.utils.services.DownloadFileService
import com.advancednotes.utils.services.RecordAudioService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SingleNoteViewModel @Inject constructor(
    private val notesUseCase: NotesUseCase,
    private val tagsUseCase: TagsUseCase,
    private val locationUseCase: LocationUseCase,
    private val notesFilesUseCase: NotesFilesUseCase,
    private val noteRecordAudioNotificationHelper: NoteRecordAudioNotificationHelper,
    private val stickyNoteNotificationHelper: StickyNoteNotificationHelper,
) : ViewModel() {

    private val _singleNoteState = MutableStateFlow(SingleNoteState())
    val singleNoteState: StateFlow<SingleNoteState> = _singleNoteState.asStateFlow()

    val snackbarRecordingAudioChannel = Channel<Unit>(Channel.CONFLATED)
    val snackbarUndoLocationChannel = Channel<Unit>(Channel.CONFLATED)
    val snackbarDiscardChangesChannel = Channel<Unit>(Channel.CONFLATED)
    val refreshRecordsChannel = Channel<Unit>(Channel.CONFLATED)
    val exitChannel = Channel<Unit>(Channel.CONFLATED)

    fun getNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val noteDeferred: Deferred<Note> =
                async(Dispatchers.IO) { notesUseCase.getNoteByUUID(noteUUID) }
            val tagsDeferred: Deferred<List<Tag>> = async(Dispatchers.IO) { tagsUseCase.getTags() }

            withContext(Dispatchers.Main) {
                _singleNoteState.value = _singleNoteState.value.copy(
                    isLoading = false,
                    note = noteDeferred.await(),
                    tags = tagsDeferred.await()
                )
            }
        }
    }

    fun getGalleryImages(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteImages: List<NoteFile> =
                notesFilesUseCase.getNoteFilesFromDirectory(
                    noteUUID = noteUUID,
                    directory = NoteDirectory.IMAGES
                )

            withContext(Dispatchers.Main) {
                _singleNoteState.value =
                    _singleNoteState.value.copy(
                        noteImages = noteImages
                    )
            }
        }
    }

    fun getRecords(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteRecords: List<NoteFile> =
                notesFilesUseCase.getNoteFilesFromDirectory(
                    noteUUID = noteUUID,
                    directory = NoteDirectory.AUDIO
                )

            withContext(Dispatchers.Main) {
                _singleNoteState.value =
                    _singleNoteState.value.copy(
                        noteRecords = noteRecords
                    )
            }
        }
    }

    fun getStickyNote(context: Context, noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isStickyNote: Boolean =
                context.getStickyNoteNotificationByNoteUUID(noteUUID) != null

            withContext(Dispatchers.Main) {
                _singleNoteState.value =
                    _singleNoteState.value.copy(
                        isStickyNote = isStickyNote
                    )
            }
        }
    }

    fun renameRecord(
        noteRecord: NoteFile,
        newFileName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            notesFilesUseCase.updateNoteFileName(noteRecord.uuid, newFileName)

            refreshRecords()
        }
    }

    fun deleteRecord(noteRecord: NoteFile) {
        viewModelScope.launch(Dispatchers.IO) {
            notesFilesUseCase.updateNoteFileDeletedPermanently(noteRecord.uuid)

            refreshRecords()
        }
    }

    fun downloadRecord(context: Context, noteFile: NoteFile) {
        val extras: Bundle = bundleOf(
            "noteFileUUID" to noteFile.uuid
        )

        val mIntent: Intent = Intent(context, DownloadFileService::class.java)
            .setAction(ACTION_START_SERVICE)
            .putExtras(extras)

        context.startService(mIntent)
    }

    fun changeStickyNote() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_singleNoteState.value.isStickyNote) {
                stickyNoteNotificationHelper.dismissStickyNoteNotificationIfExistsByNoteUUID(
                    _singleNoteState.value.note.uuid
                )

                _singleNoteState.value = _singleNoteState.value.copy(
                    isStickyNote = false
                )
            } else {
                stickyNoteNotificationHelper.sendStickyNoteNotification(
                    noteUUID = _singleNoteState.value.note.uuid,
                    noteName = _singleNoteState.value.note.name
                )

                _singleNoteState.value = _singleNoteState.value.copy(
                    isStickyNote = true
                )
            }
        }
    }

    fun changeNoteName(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    name = name
                ),
                hasChanges = true
            )
        }
    }

    fun changeAdvancedContent(advancedContent: List<AdvancedContentItem>?) {
        viewModelScope.launch(Dispatchers.Default) {
            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    advancedContent = advancedContent
                ),
                hasChanges = true
            )
        }
    }

    fun selectTag(tagUUID: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val tagUUIDInList: Boolean =
                _singleNoteState.value.note.tagsUUIDs.any { it == tagUUID }

            val newNoteTagsUUIDs: List<String> = if (tagUUIDInList) {
                _singleNoteState.value.note.tagsUUIDs - tagUUID
            } else {
                _singleNoteState.value.note.tagsUUIDs + tagUUID
            }

            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    tagsUUIDs = newNoteTagsUUIDs
                ),
                hasChanges = true
            )
        }
    }

    fun changeNoteLocation(delete: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (delete) {
                withContext(Dispatchers.Default) {
                    val previousLocation = _singleNoteState.value.note.location

                    _singleNoteState.value = _singleNoteState.value.copy(
                        note = _singleNoteState.value.note.copy(
                            location = null
                        ),
                        previousLocation = previousLocation,
                        hasChanges = true
                    )

                    showSnackbarUndoLocationChannel()
                }
            } else {
                val previousLocation = _singleNoteState.value.note.location

                _singleNoteState.value = _singleNoteState.value.copy(
                    gettingLocation = true
                )

                locationUseCase.getLocation { myLocation ->
                    viewModelScope.launch(Dispatchers.Default) {
                        _singleNoteState.value = _singleNoteState.value.copy(
                            gettingLocation = false,
                            note = _singleNoteState.value.note.copy(
                                location = myLocation
                            ),
                            previousLocation = previousLocation,
                            hasChanges = true
                        )

                        if (_singleNoteState.value.previousLocation != null) {
                            showSnackbarUndoLocationChannel()
                        }
                    }
                }
            }
        }
    }

    fun undoLocation() {
        val previousLocation = _singleNoteState.value.previousLocation

        _singleNoteState.value = _singleNoteState.value.copy(
            note = _singleNoteState.value.note.copy(
                location = previousLocation
            ),
            previousLocation = previousLocation
        )
    }

    fun changeReminder(reminder: NoteReminder?) {
        viewModelScope.launch(Dispatchers.Default) {
            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    reminder = reminder
                ),
                hasChanges = true
            )
        }
    }

    fun changeNoteFixed() {
        viewModelScope.launch(Dispatchers.Default) {
            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    fixed = !_singleNoteState.value.note.fixed
                ),
                hasChanges = true
            )
        }
    }

    fun changeNoteArchived() {
        viewModelScope.launch(Dispatchers.Default) {
            _singleNoteState.value = _singleNoteState.value.copy(
                note = _singleNoteState.value.note.copy(
                    archived = !_singleNoteState.value.note.archived
                ),
                hasChanges = true
            )
        }
    }

    fun trashNote(noteUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.updateNoteTrash(
                uuid = noteUUID,
                trash = true
            )

            exit()
        }
    }

    fun createTempPictureFile(context: Context): Uri {
        val fileName: String = "temp_${getCurrentDateForFileName()}"
        val directory: File = getCacheFilesDirectory(context)
        val tempFile: File =
            File.createTempFile(fileName, ".jpg", directory)

        return FileProvider.getUriForFile(
            context,
            Constants.FILE_PROVIDER_AUTHORITY,
            tempFile
        )
    }

    fun onPictureCaptured(context: Context, tempFileUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (tempFileUri != null) {
                val noteFile = NoteFile(
                    noteUUID = _singleNoteState.value.note.uuid,
                    fileExtension = "jpg",
                    directory = NoteDirectory.IMAGES
                )

                val fileName: String = "temp_${getCurrentDateForFileName()}"
                val directory: File = getCacheFilesDirectory(context)
                val tempFile: File =
                    File.createTempFile(fileName, ".jpg", directory)

                tempFileUri.let { context.contentResolver.openInputStream(it) }.use { input ->
                    tempFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

                notesFilesUseCase.insertNoteFile(
                    noteFile,
                    tempFile
                )

                tempFile.deleteRecursively()

                getGalleryImages(_singleNoteState.value.note.uuid)
            }
        }
    }

    fun setRecordingAudio(active: Boolean, context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            val previousRecordingAudio: Boolean = _singleNoteState.value.recordingAudio

            _singleNoteState.value = _singleNoteState.value.copy(
                recordingAudio = active
            )

            delay(300)

            if (active) {
                startRecordService(context)
            } else if (previousRecordingAudio) {
                stopRecordService(context)
            }
        }
    }

    private fun startRecordService(context: Context) {
        val extras: Bundle = bundleOf(
            "noteUUID" to _singleNoteState.value.note.uuid
        )

        val mIntent: Intent = Intent(context, RecordAudioService::class.java)
            .setAction(ACTION_START_SERVICE)
            .putExtras(extras)

        context.startService(mIntent)
    }

    private fun stopRecordService(context: Context) {
        val mIntent: Intent = Intent(context, RecordAudioService::class.java)
            .setAction(ACTION_STOP_SERVICE)

        context.startService(mIntent)
    }

    fun notificationsAreAvailable(): Boolean {
        return noteRecordAudioNotificationHelper.notificationsAreAvailable()
    }

    fun recordServiceIsActive(): Boolean {
        return noteRecordAudioNotificationHelper.noteRecordNotificationIsActive()
    }

    fun showSnackBarRecordingAudio() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarRecordingAudioChannel.trySend(Unit)
        }
    }

    fun showSnackbarUndoLocationChannel() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarUndoLocationChannel.trySend(Unit)
        }
    }

    fun showSnackbarDiscardChangesChannel() {
        viewModelScope.launch(Dispatchers.Default) {
            snackbarDiscardChangesChannel.trySend(Unit)
        }
    }

    private fun refreshRecords() {
        viewModelScope.launch(Dispatchers.Default) {
            refreshRecordsChannel.trySend(Unit)
        }
    }

    fun getPathFromNoteFile(noteFile: NoteFile): String? {
        val file: File? = notesFilesUseCase.getFile(noteFile)

        return file?.absolutePath
    }

    private fun isNewNote(): Boolean = _singleNoteState.value.note.uuid == EMPTY_UUID

    fun isNewNoteWithEmptyData(): Boolean {
        if (!isNewNote()) {
            return false
        }

        if (
            _singleNoteState.value.note.isEmpty() &&
            _singleNoteState.value.noteImages.isEmpty() &&
            _singleNoteState.value.noteRecords.isEmpty()
        ) {
            return true
        }

        return false
    }

    fun validateData(): Boolean {
        return _singleNoteState.value.note.name.isNotEmpty()
    }

    fun saveNoteAndExit() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNewNote()) {
                val newNoteId: Long = notesUseCase.insertNote(_singleNoteState.value.note)
                val newNoteUUID: String = notesUseCase.getNoteUUIDById(newNoteId)
                moveNoteFilesFromTempDir(newNoteUUID)
            } else {
                if (_singleNoteState.value.hasChanges) {
                    notesUseCase.updateNote(_singleNoteState.value.note)
                }

                if (_singleNoteState.value.isStickyNote) {
                    stickyNoteNotificationHelper.sendStickyNoteNotification(
                        noteUUID = _singleNoteState.value.note.uuid,
                        noteName = _singleNoteState.value.note.name
                    )
                }
            }

            exit()
        }
    }

    fun exit() {
        viewModelScope.launch(Dispatchers.IO) {
            cleanNoteFilesFromTempDir()
            exitChannel.trySend(Unit)
        }
    }

    private fun moveNoteFilesFromTempDir(newNoteUUID: String) {
        notesFilesUseCase.duplicateNoteFiles(
            oldNoteUUID = EMPTY_UUID,
            newNoteUUID = newNoteUUID
        )
    }

    private fun cleanNoteFilesFromTempDir() {
        val noteFilesFromTempDir: List<NoteFile> =
            notesFilesUseCase.getAllNoteFiles(EMPTY_UUID)

        noteFilesFromTempDir.forEach { noteFile ->
            notesFilesUseCase.deleteNoteFile(noteFile)
        }
    }
}