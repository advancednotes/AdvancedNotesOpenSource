package com.advancednotes.utils.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.advancednotes.data.datastore.deleteReminderNotification
import com.advancednotes.data.datastore.getRemindersNotifications
import com.advancednotes.data.datastore.saveReminderNotification
import com.advancednotes.domain.models.NoteOnlyReminders
import com.advancednotes.domain.models.ReminderNotification
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.utils.date.DateHelper.dateIsOld
import com.advancednotes.utils.receivers.ReminderNotificationReceiver
import com.advancednotes.utils.uuid.UUIDHelper.generateNotificationUUID
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SetRemindersService @Inject constructor() : Service() {

    private var startMode: Int = START_STICKY
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false

    private lateinit var alarmManager: AlarmManager

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var notesUseCase: NotesUseCase

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        coroutineScope.launch(Dispatchers.IO) {
            cancelCurrentNoteReminders()
            setNoteReminders()
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

    private fun cancelCurrentNoteReminders() {
        val remindersNotifications: List<ReminderNotification> = getRemindersNotifications()

        remindersNotifications.forEach { reminderNotification ->
            val mIntent: Intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
                putExtra("noteUUID", reminderNotification.noteUUID)
            }

            val pendingIntent: PendingIntent? = PendingIntent.getBroadcast(
                context,
                reminderNotification.reminderNotificationUUID,
                mIntent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                deleteReminderNotification(reminderNotification)
            }
        }
    }

    private fun setNoteReminders() {
        val notesReminders: List<NoteOnlyReminders> = notesUseCase.getNotesReminders()

        notesReminders.forEach { noteReminder ->
            if (noteReminder.reminder != null) {
                if (!dateIsOld(noteReminder.reminder.reminderDate)) {
                    val reminderNotificationUUID: Int = generateNotificationUUID()

                    val mIntent: Intent =
                        Intent(context, ReminderNotificationReceiver::class.java).apply {
                            putExtra("noteUUID", noteReminder.uuid)
                        }

                    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                        context,
                        reminderNotificationUUID,
                        mIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    try {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            noteReminder.reminder.reminderDate.time,
                            pendingIntent
                        )

                        saveReminderNotification(
                            ReminderNotification(
                                reminderNotificationUUID = reminderNotificationUUID,
                                noteUUID = noteReminder.uuid
                            )
                        )

                    } catch (_: SecurityException) {
                    }
                }
            }
        }
    }

    private fun stopService() {
        coroutineScope.cancel()
        stopSelf()
    }
}