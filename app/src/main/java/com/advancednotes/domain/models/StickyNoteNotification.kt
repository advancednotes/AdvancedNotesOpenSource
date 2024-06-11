package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StickyNoteNotification(
    @SerializedName("stickyNoteNotificationUUID") val stickyNoteNotificationUUID: Int,
    @SerializedName("noteUUID") val noteUUID: String
)