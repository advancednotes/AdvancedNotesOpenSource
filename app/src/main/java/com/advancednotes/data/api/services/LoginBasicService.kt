package com.advancednotes.data.api.services

import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.LoginResponseModel
import com.advancednotes.data.api.models.VerifyEmailResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginBasicService {

    @FormUrlEncoded
    @POST
    fun verifyEmailFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<VerifyEmailResponseModel>>

    @FormUrlEncoded
    @POST
    fun loginWithEmailAndPasswordFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<LoginResponseModel>>

    @FormUrlEncoded
    @POST
    fun registerWithEmailAndPasswordFromApi(
        @Url endpoint: String,
        @Field("data") data: String
    ): Call<ApiResponseModel<LoginResponseModel>>
}