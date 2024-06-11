package com.advancednotes.data.api.services

import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface UserService {

    @Keep
    @FormUrlEncoded
    @POST
    fun updateFirstName(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<Void>

    @Keep
    @FormUrlEncoded
    @POST
    fun updateLastName(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<Void>

    @Keep
    @FormUrlEncoded
    @POST
    fun updatePassword(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<Void>
}