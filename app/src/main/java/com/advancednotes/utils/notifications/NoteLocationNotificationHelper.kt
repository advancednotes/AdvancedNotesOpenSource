package com.advancednotes.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.ACTION_STOP_SERVICE
import com.advancednotes.common.Constants.Companion.STOP_SERVICE_REQUEST_CODE
import com.advancednotes.utils.services.LocationService
import javax.inject.Inject

class NoteLocationNotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        const val LOCATION_NOTIFICATION_ID: Int = 7040
        private const val CHANNEL_ID = "note_location_channel"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification(): Notification {
        createNotificationChannel()

        val stopServiceIntent: Intent = Intent(context, LocationService::class.java)
            .setAction(ACTION_STOP_SERVICE)

        val stopServicePendingIntent: PendingIntent =
            PendingIntent.getService(
                context,
                STOP_SERVICE_REQUEST_CODE,
                stopServiceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.location_background_message))
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                R.drawable.baseline_stop_24,
                context.getString(R.string.cancel_button),
                stopServicePendingIntent
            )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name: String = context.getString(R.string.location_channel_name)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    setShowBadge(true)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }
}