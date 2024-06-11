package com.advancednotes.data.api.services

import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.SynchronizeNoteFileResponseModel
import com.advancednotes.data.api.models.SynchronizeNoteResponseModel
import com.advancednotes.data.api.models.SynchronizeTagResponseModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface SynchronizerService {

    @FormUrlEncoded
    @POST
    fun syncNoteFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<SynchronizeNoteResponseModel>>

    @POST
    fun syncNoteFileFromApi(
        @Url endpoint: String,
        @Body requestBody: RequestBody
    ): Call<ApiResponseModel<SynchronizeNoteFileResponseModel?>>

    @FormUrlEncoded
    @POST
    fun syncTagFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<SynchronizeTagResponseModel?>>
}