package com.advancednotes.utils.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.advancednotes.domain.models.Frequency
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.utils.date.DateHelper.addOneDayToDate
import com.advancednotes.utils.date.DateHelper.addOneMonthToDate
import com.advancednotes.utils.date.DateHelper.addOneWeekToDate
import com.advancednotes.utils.date.DateHelper.addOneYearToDate
import com.advancednotes.utils.notifications.NoteReminderNotificationHelper
import com.advancednotes.utils.services.SetRemindersService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ReminderNotificationReceiver : BroadcastReceiver() {

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    @Inject
    lateinit var notesUseCase: NotesUseCase

    @Inject
    lateinit var noteReminderNotificationHelper: NoteReminderNotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val noteUUID: String = intent.getStringExtra("noteUUID") ?: ""

        coroutineScope.launch(Dispatchers.IO) {
            val note: Note = notesUseCase.getNoteByUUID(noteUUID)

            val reminderUpdated: Boolean = when (note.reminder?.frequency) {
                Frequency.DAILY -> {
                    notesUseCase.updateNoteReminder(
                        noteUUID,
                        note.reminder.copy(reminderDate = addOneDayToDate(note.reminder.reminderDate))
                    )

                    true
                }

                Frequency.WEEKLY -> {
                    notesUseCase.updateNoteReminder(
                        noteUUID,
                        note.reminder.copy(reminderDate = addOneWeekToDate(note.reminder.reminderDate))
                    )

                    true
                }

                Frequency.MONTHLY -> {
                    notesUseCase.updateNoteReminder(
                        noteUUID,
                        note.reminder.copy(reminderDate = addOneMonthToDate(note.reminder.reminderDate))
                    )

                    true
                }

                Frequency.YEARLY -> {
                    notesUseCase.updateNoteReminder(
                        noteUUID,
                        note.reminder.copy(reminderDate = addOneYearToDate(note.reminder.reminderDate))
                    )

                    true
                }

                else -> false
            }

            withContext(Dispatchers.Main) {
                noteReminderNotificationHelper.sendNoteReminderNotification(
                    noteUUID = noteUUID,
                    noteName = note.name,
                )

                if (reminderUpdated) {
                    startSetRemindersService(context)
                }
            }
        }
    }

    private fun startSetRemindersService(context: Context) {
        Intent(context, SetRemindersService::class.java).also { mIntent ->
            context.startService(mIntent)
        }
    }
}