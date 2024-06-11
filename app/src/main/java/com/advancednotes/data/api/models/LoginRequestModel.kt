package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginRequestModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)