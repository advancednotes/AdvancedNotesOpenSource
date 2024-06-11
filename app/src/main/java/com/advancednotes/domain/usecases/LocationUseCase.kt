package com.advancednotes.domain.usecases

import com.advancednotes.domain.models.MyLocation

interface LocationUseCase {

    suspend fun getLocation(onLocationReceived: (myLocation: MyLocation) -> Unit)
}