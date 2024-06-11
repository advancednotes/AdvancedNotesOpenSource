package com.advancednotes.data.api.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface DownloaderService {

    @GET
    fun downloadFile(
        @Url endpoint: String
    ): Call<ResponseBody>
}