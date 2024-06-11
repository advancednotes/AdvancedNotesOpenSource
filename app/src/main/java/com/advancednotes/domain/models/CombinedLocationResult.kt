package com.advancednotes.domain.models

import androidx.annotation.Keep

@Keep
data class CombinedLocationResult(
    val fusedLocation: MyLocation = MyLocation(),
    val gpsLocation: MyLocation = MyLocation(),
    val networkLocation: MyLocation = MyLocation()
)