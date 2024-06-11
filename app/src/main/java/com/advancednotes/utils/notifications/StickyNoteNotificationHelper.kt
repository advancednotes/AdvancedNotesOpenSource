package com.advancednotes.utils.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.ACTION_DELETE_NOTIFICATION
import com.advancednotes.common.Constants.Companion.ACTION_START_SERVICE
import com.advancednotes.data.datastore.getStickyNoteNotificationByNoteUUID
import com.advancednotes.data.datastore.saveStickyNoteNotification
import com.advancednotes.domain.models.StickyNoteNotification
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivity
import com.advancednotes.utils.receivers.DismissStickyNoteNotificationReceiver
import com.advancednotes.utils.services.LocationService
import com.advancednotes.utils.services.RecordAudioService
import com.advancednotes.utils.uuid.UUIDHelper.generateNotificationUUID
import javax.inject.Inject

class StickyNoteNotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "sticky_note_channel_id"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendStickyNoteNotification(
        noteUUID: String,
        noteName: String
    ) {
        val stickyNoteNotification: StickyNoteNotification =
            context.getStickyNoteNotificationByNoteUUID(noteUUID) ?: StickyNoteNotification(
                stickyNoteNotificationUUID = generateNotificationUUID(),
                noteUUID = noteUUID
            )

        notificationManager.notify(
            stickyNoteNotification.stickyNoteNotificationUUID,
            getNotification(
                stickyNoteNotification = stickyNoteNotification,
                noteName = noteName
            )
        )

        context.saveStickyNoteNotification(stickyNoteNotification)
    }

    fun dismissStickyNoteNotificationIfExistsByNoteUUID(noteUUID: String) {
        val stickyNoteNotification: StickyNoteNotification? =
            context.getStickyNoteNotificationByNoteUUID(noteUUID)

        if (stickyNoteNotification != null) {
            val dismissStickyNoteIntent: Intent =
                Intent(context, DismissStickyNoteNotificationReceiver::class.java)
                    .setAction(ACTION_DELETE_NOTIFICATION)
                    .putExtras(
                        bundleOf(
                            "stickyNoteNotificationUUID" to stickyNoteNotification.stickyNoteNotificationUUID,
                            "noteUUID" to stickyNoteNotification.noteUUID
                        )
                    )

            context.sendBroadcast(dismissStickyNoteIntent)
        }
    }

    private fun getNotification(
        stickyNoteNotification: StickyNoteNotification,
        noteName: String
    ): Notification {
        createNotificationChannel()

        val deepLinkIntent: Intent = Intent(
            Intent.ACTION_VIEW,
            "advancednotes://${Screen.SingleNoteScreen.route}?noteUUID=${stickyNoteNotification.noteUUID}".toUri(),
            context,
            MainActivity::class.java
        )

        val locationIntent: Intent = Intent(context, LocationService::class.java)
            .setAction(ACTION_START_SERVICE)
            .putExtras(
                bundleOf(
                    "noteUUID" to stickyNoteNotification.noteUUID
                )
            )

        val recordAudioIntent: Intent = Intent(context, RecordAudioService::class.java)
            .setAction(ACTION_START_SERVICE)
            .putExtras(
                bundleOf(
                    "noteUUID" to stickyNoteNotification.noteUUID
                )
            )

        val dismissStickyNoteIntent: Intent =
            Intent(context, DismissStickyNoteNotificationReceiver::class.java)
                .setAction(ACTION_DELETE_NOTIFICATION)
                .putExtras(
                    bundleOf(
                        "stickyNoteNotificationUUID" to stickyNoteNotification.stickyNoteNotificationUUID,
                        "noteUUID" to stickyNoteNotification.noteUUID
                    )
                )

        val deepLinkPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                stickyNoteNotification.stickyNoteNotificationUUID,
                deepLinkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val locationPendingIntent: PendingIntent =
            PendingIntent.getService(
                context,
                stickyNoteNotification.stickyNoteNotificationUUID,
                locationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val recordAudioPendingIntent: PendingIntent =
            PendingIntent.getService(
                context,
                stickyNoteNotification.stickyNoteNotificationUUID,
                recordAudioIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val dismissStickyNotePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                stickyNoteNotification.stickyNoteNotificationUUID,
                dismissStickyNoteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(noteName)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(deepLinkPendingIntent)

        if (locationPermissionsIsGranted()) {
            notificationBuilder.addAction(
                R.drawable.logo,
                context.getString(R.string.note_location),
                locationPendingIntent
            )
        }

        if (recordPermissionsIsGranted()) {
            notificationBuilder.addAction(
                R.drawable.logo,
                context.getString(R.string.note_record),
                recordAudioPendingIntent
            )
        }

        notificationBuilder.addAction(
            android.R.drawable.ic_menu_close_clear_cancel,
            context.getString(R.string.close_button),
            dismissStickyNotePendingIntent
        )

        notificationBuilder
            .setAutoCancel(false)
            .setOngoing(true)
            .setSilent(true)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name: String = context.getString(R.string.sticky_notifications_channel_name)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    setShowBadge(false)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun locationPermissionsIsGranted(): Boolean {
        val permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return true
    }

    private fun recordPermissionsIsGranted(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECORD_AUDIO
            )
        } else {
            listOf(
                Manifest.permission.RECORD_AUDIO
            )
        }

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return true
    }
}