package com.advancednotes.utils.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.advancednotes.utils.services.SetRemindersService
import com.advancednotes.utils.services.SetStickyNotesService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Intent(context, SetRemindersService::class.java).also { mIntent ->
                context.startService(mIntent)
            }

            Intent(context, SetStickyNotesService::class.java).also { mIntent ->
                context.startService(mIntent)
            }
        }
    }
}