package com.advancednotes.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.advancednotes.R
import javax.inject.Inject

class DownloadFileNotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        const val DOWNLOAD_FILE_NOTIFICATION_ID: Int = 7320
        private const val CHANNEL_ID = "download_file_channel"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification(fileName: String): Notification {
        createNotificationChannel()

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.downloading_file))
            .setContentText(fileName)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name: String = context.getString(R.string.download_file_channel_name)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    setShowBadge(true)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }
}