package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PreSynchronizeItem(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("modificationDate") val modificationDate: String
)