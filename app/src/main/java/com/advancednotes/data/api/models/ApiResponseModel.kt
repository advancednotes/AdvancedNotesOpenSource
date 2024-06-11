package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiResponseModel<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("body") val body: T?
)