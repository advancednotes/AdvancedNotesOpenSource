package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SynchronizeTagRequestModel(
    @SerializedName("tagUUID") val tagUUID: String,
    @SerializedName("tagToSynchronize") val tagToSynchronize: TagModel?
)