package com.advancednotes.utils.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.os.bundleOf
import com.advancednotes.common.Constants.Companion.ACTION_START_SERVICE
import com.advancednotes.common.Constants.Companion.ACTION_STOP_SERVICE
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.usecases.LocationUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.utils.notifications.NoteLocationNotificationHelper
import com.advancednotes.utils.notifications.NoteLocationNotificationHelper.Companion.LOCATION_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LocationService @Inject constructor() : Service() {

    private var startMode: Int = START_STICKY
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var notesUseCase: NotesUseCase

    @Inject
    lateinit var noteLocationNotificationHelper: NoteLocationNotificationHelper

    @Inject
    lateinit var locationUseCase: LocationUseCase

    private var noteUUID: String = EMPTY_UUID

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_SERVICE -> {
                val extras: Bundle = intent.extras ?: bundleOf()
                noteUUID = extras.getString("noteUUID", EMPTY_UUID)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startForeground(
                        LOCATION_NOTIFICATION_ID,
                        noteLocationNotificationHelper.getNotification(),
                        FOREGROUND_SERVICE_TYPE_LOCATION
                    )
                } else {
                    startForeground(
                        LOCATION_NOTIFICATION_ID,
                        noteLocationNotificationHelper.getNotification()
                    )
                }

                updateNoteLocation()
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

    private fun updateNoteLocation() {
        coroutineScope.launch(Dispatchers.IO) {
            locationUseCase.getLocation { myLocation ->
                coroutineScope.launch {
                    notesUseCase.updateNoteLocation(
                        uuid = noteUUID,
                        location = myLocation
                    )

                    withContext(Dispatchers.Default) {
                        delay(1000)
                        stopService()
                    }
                }
            }
        }
    }

    private fun stopService() {
        coroutineScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}