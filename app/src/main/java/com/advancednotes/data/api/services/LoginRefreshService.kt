package com.advancednotes.data.api.services

import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.LoginResponseModel
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginRefreshService {

    @POST
    fun loginWithRefreshTokenFromApi(
        @Url endpoint: String
    ): Call<ApiResponseModel<LoginResponseModel>>
}