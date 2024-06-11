package com.advancednotes.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.advancednotes.R
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivity
import com.advancednotes.utils.uuid.UUIDHelper.generateNotificationUUID
import javax.inject.Inject

class NoteReminderNotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "reminder_channel"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNoteReminderNotification(
        noteUUID: String,
        noteName: String
    ) {
        notificationManager.notify(
            generateNotificationUUID(),
            getNotification(
                noteUUID = noteUUID,
                noteName = noteName
            )
        )
    }

    private fun getNotification(
        noteUUID: String,
        noteName: String
    ): Notification {
        createNotificationChannel()

        val intent: Intent = Intent(
            Intent.ACTION_VIEW,
            "advancednotes://${Screen.SingleNoteScreen.route}?noteUUID=${noteUUID}".toUri(),
            context,
            MainActivity::class.java
        )

        val deepLinkPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                generateNotificationUUID(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(noteName)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(deepLinkPendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name: String = context.getString(R.string.reminder_channel_name)
            val importance: Int = NotificationManager.IMPORTANCE_HIGH

            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    enableVibration(true)
                    setShowBadge(true)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }
}