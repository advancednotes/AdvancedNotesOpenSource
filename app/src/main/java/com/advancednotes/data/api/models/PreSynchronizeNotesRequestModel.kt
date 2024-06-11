package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PreSynchronizeNotesRequestModel(
    @SerializedName("preSynchronizeItems") val preSynchronizeItems: List<PreSynchronizeItem>
)