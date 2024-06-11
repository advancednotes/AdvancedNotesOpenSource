package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserFirstNameRequestModel(
    @SerializedName("firstName") val firstName: String
)