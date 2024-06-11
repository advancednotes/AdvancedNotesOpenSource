package com.advancednotes.utils.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.os.bundleOf
import com.advancednotes.common.Constants.Companion.ACTION_START_SERVICE
import com.advancednotes.common.Constants.Companion.ACTION_STOP_SERVICE
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.utils.date.DateHelper.getCurrentDateForFileName
import com.advancednotes.utils.files.FileHelper.getCacheFilesDirectory
import com.advancednotes.utils.notifications.NoteRecordAudioNotificationHelper
import com.advancednotes.utils.notifications.NoteRecordAudioNotificationHelper.Companion.RECORD_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RecordAudioService @Inject constructor() : Service() {

    private var startMode: Int = START_STICKY
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var notesFilesUseCase: NotesFilesUseCase

    @Inject
    lateinit var noteRecordAudioNotificationHelper: NoteRecordAudioNotificationHelper

    private var noteUUID: String = EMPTY_UUID

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var tempFile: File

    companion object {
        private val _recordAdded = MutableSharedFlow<Boolean>()
        val recordAdded = _recordAdded.asSharedFlow()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_SERVICE -> {
                val extras: Bundle = intent.extras ?: bundleOf()
                noteUUID = extras.getString("noteUUID", EMPTY_UUID)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startForeground(
                        RECORD_NOTIFICATION_ID,
                        noteRecordAudioNotificationHelper.getNotification(),
                        FOREGROUND_SERVICE_TYPE_MICROPHONE
                    )
                } else {
                    startForeground(
                        RECORD_NOTIFICATION_ID,
                        noteRecordAudioNotificationHelper.getNotification()
                    )
                }

                createTempRecord()
                startRecording()
            }

            ACTION_STOP_SERVICE -> {
                stopRecording()
            }
        }

        return startMode
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }

    private fun startRecording() {
        coroutineScope.launch {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else MediaRecorder()

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setOutputFile(tempFile.absolutePath)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                mediaRecorder.prepare()
                mediaRecorder.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        coroutineScope.launch {
            try {
                mediaRecorder.stop()
                mediaRecorder.release()

                val recordSaved: Deferred<Boolean> = async(Dispatchers.IO) {
                    val noteFile = NoteFile(
                        noteUUID = noteUUID,
                        fileExtension = "mp3",
                        directory = NoteDirectory.AUDIO
                    )

                    notesFilesUseCase.insertNoteFile(
                        noteFile,
                        tempFile
                    )

                    tempFile.deleteRecursively()

                    true
                }

                if (recordSaved.await()) {
                    _recordAdded.emit(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            stopService()
        }
    }

    private fun createTempRecord() {
        val fileName = "temp_${getCurrentDateForFileName()}"
        val directory: File = getCacheFilesDirectory(context)
        tempFile = File.createTempFile(fileName, ".mp3", directory)
    }

    private fun stopService() {
        coroutineScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}