package com.advancednotes.utils.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.advancednotes.data.datastore.getStickyNotesNotifications
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.StickyNoteNotification
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.utils.notifications.StickyNoteNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SetStickyNotesService @Inject constructor() : Service() {

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
    lateinit var stickyNoteNotificationHelper: StickyNoteNotificationHelper

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        coroutineScope.launch(Dispatchers.IO) {
            val stickyNotesNotifications: List<StickyNoteNotification> =
                getStickyNotesNotifications()

            stickyNotesNotifications.forEach { stickyNoteNotification ->
                val note: Note = notesUseCase.getNoteByUUID(stickyNoteNotification.noteUUID)

                withContext(Dispatchers.Main) {
                    stickyNoteNotificationHelper.sendStickyNoteNotification(
                        noteUUID = stickyNoteNotification.noteUUID,
                        noteName = note.name
                    )
                }
            }
        }.invokeOnCompletion {
            stopService()
        }

        return startMode
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }

    private fun stopService() {
        coroutineScope.cancel()
        stopSelf()
    }
}