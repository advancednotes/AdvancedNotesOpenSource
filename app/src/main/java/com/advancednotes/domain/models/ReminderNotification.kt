package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReminderNotification(
    @SerializedName("reminderNotificationUUID") val reminderNotificationUUID: Int,
    @SerializedName("noteUUID") val noteUUID: String
)
