package com.advancednotes.utils.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.advancednotes.common.Constants.Companion.ACTION_DELETE_NOTIFICATION
import com.advancednotes.data.datastore.deleteStickyNoteNotification
import com.advancednotes.domain.models.StickyNoteNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DismissStickyNoteNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_DELETE_NOTIFICATION) {
            val stickyNoteNotificationUUID: Int =
                intent.getIntExtra("stickyNoteNotificationUUID", 0)
            val noteUUID: String = intent.getStringExtra("noteUUID") ?: ""

            val stickyNoteNotification = StickyNoteNotification(
                stickyNoteNotificationUUID = stickyNoteNotificationUUID,
                noteUUID = noteUUID
            )

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(stickyNoteNotificationUUID)
            context.deleteStickyNoteNotification(stickyNoteNotification)
        }
    }
}