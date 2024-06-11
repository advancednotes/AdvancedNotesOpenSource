package com.advancednotes.domain.models

import androidx.annotation.Keep

@Keep
data class MyLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f
)

fun MyLocation.isNotEmpty(): Boolean {
    return this.latitude != 0.0 && this.longitude != 0.0
}