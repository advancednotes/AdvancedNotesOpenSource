package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserPasswordRequestModel(
    @SerializedName("password") val password: String
)