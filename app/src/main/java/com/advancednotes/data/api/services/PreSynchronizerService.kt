package com.advancednotes.data.api.services

import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.PreSynchronizeNotesFilesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeNotesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeTagsResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface PreSynchronizerService {

    @FormUrlEncoded
    @POST
    fun preSyncNotesFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<PreSynchronizeNotesResponseModel>>

    @FormUrlEncoded
    @POST
    fun preSyncNotesFilesFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<PreSynchronizeNotesFilesResponseModel>>

    @FormUrlEncoded
    @POST
    fun preSyncTagsFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<PreSynchronizeTagsResponseModel>>
}