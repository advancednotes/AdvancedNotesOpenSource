package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AdvancedContentItem(
    @SerializedName("type") val type: AdvancedContentType,
    @SerializedName("content") var content: String = ""
)