package com.advancednotes.data.api.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface LoginAccessService {

    @GET
    fun logoutFromApi(
        @Url endpoint: String
    ): Call<Unit>
}