package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginFailedResponseModel(
    @SerializedName("userExists") val userExists: Boolean,
    @SerializedName("availableAttempts") val availableAttempts: Int,
    @SerializedName("waitingTimeInMs") val waitingTimeInMs: Long?
)