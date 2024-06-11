package com.advancednotes.utils.notifications

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.advancednotes.R
import com.advancednotes.utils.uuid.UUIDHelper.generateNotificationUUID
import javax.inject.Inject

class DownloadedFileNotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "downloaded_file_channel"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNoteReminderNotification(
        fileName: String
    ) {
        notificationManager.notify(
            generateNotificationUUID(),
            getNotification(fileName)
        )
    }

    private fun getNotification(fileName: String): Notification {
        createNotificationChannel()

        val intent: Intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                generateNotificationUUID(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.downloaded_file))
            .setContentText(fileName)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name: String = context.getString(R.string.downloaded_file_channel_name)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    setShowBadge(true)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }
}