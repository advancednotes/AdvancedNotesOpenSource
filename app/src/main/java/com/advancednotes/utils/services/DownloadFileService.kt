package com.advancednotes.utils.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.os.bundleOf
import com.advancednotes.common.Constants.Companion.ACTION_START_SERVICE
import com.advancednotes.common.Constants.Companion.ACTION_STOP_SERVICE
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.mediastore.MediaStoreHelper
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.utils.notifications.DownloadFileNotificationHelper
import com.advancednotes.utils.notifications.DownloadFileNotificationHelper.Companion.DOWNLOAD_FILE_NOTIFICATION_ID
import com.advancednotes.utils.notifications.DownloadedFileNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFileService @Inject constructor() : Service() {

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
    lateinit var mediaStoreHelper: MediaStoreHelper

    @Inject
    lateinit var downloadFileNotificationHelper: DownloadFileNotificationHelper

    @Inject
    lateinit var downloadedFileNotificationHelper: DownloadedFileNotificationHelper

    private var noteFileUUID: String = EMPTY_UUID

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_SERVICE -> {
                coroutineScope.launch(Dispatchers.Default) {
                    val extras: Bundle = intent.extras ?: bundleOf()
                    noteFileUUID = extras.getString("noteFileUUID", EMPTY_UUID)

                    val noteFile: NoteFile = async(Dispatchers.IO) {
                        notesFilesUseCase.getNoteFileByUUID(noteFileUUID)
                    }.await()
                    val file: File? = async(Dispatchers.IO) {
                        notesFilesUseCase.getFile(noteFile)
                    }.await()

                    if (file != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            startForeground(
                                DOWNLOAD_FILE_NOTIFICATION_ID,
                                downloadFileNotificationHelper.getNotification(file.name),
                                FOREGROUND_SERVICE_TYPE_DATA_SYNC
                            )
                        } else {
                            startForeground(
                                DOWNLOAD_FILE_NOTIFICATION_ID,
                                downloadFileNotificationHelper.getNotification(file.name)
                            )
                        }

                        downloadFile(file)
                    } else {
                        stopService()
                    }
                }
            }

            ACTION_STOP_SERVICE -> {
                stopService()
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

    private suspend fun downloadFile(file: File) {
        mediaStoreHelper.saveFileToDownloads(
            file = file,
            onProgress = null
        )

        withContext(Dispatchers.Default) {
            delay(1000)
            downloadedFileNotificationHelper.sendNoteReminderNotification(file.name)
            stopService()
        }
    }

    private fun stopService() {
        coroutineScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}