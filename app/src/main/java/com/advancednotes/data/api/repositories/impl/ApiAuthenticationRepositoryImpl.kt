package com.advancednotes.data.api.repositories.impl


import com.advancednotes.BuildConfig
import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.LoginFailedResponseModel
import com.advancednotes.data.api.models.LoginRequestModel
import com.advancednotes.data.api.models.LoginResponseModel
import com.advancednotes.data.api.models.RegisterRequestModel
import com.advancednotes.data.api.models.VerifyEmailRequestModel
import com.advancednotes.data.api.models.VerifyEmailResponseModel
import com.advancednotes.data.api.repositories.ApiAuthenticationRepository
import com.advancednotes.data.api.services.LoginAccessService
import com.advancednotes.data.api.services.LoginBasicService
import com.advancednotes.data.api.services.LoginRefreshService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ApiAuthenticationRepositoryImpl @Inject constructor(
    private val loginBasicService: LoginBasicService,
    private val loginAccessService: LoginAccessService,
    private val loginRefreshService: LoginRefreshService,
    private val gson: Gson
) : ApiAuthenticationRepository {

    override fun verifyEmail(
        email: String,
        onResponse: (response: VerifyEmailResponseModel?) -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                VerifyEmailRequestModel(
                    email = email
                )
            )

            val call: Call<ApiResponseModel<VerifyEmailResponseModel>> =
                loginBasicService.verifyEmailFromApi(
                    endpoint = BuildConfig.VERIFY_EMAIL_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<VerifyEmailResponseModel>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<VerifyEmailResponseModel>? = response.body()

                when (responseCode) {
                    200 -> {
                        onResponse(responseBody?.body)
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    onBadRequest()
                }

                else -> {
                    onFailure()
                }
            }
        } catch (e: IOException) {
            onFailure()
        }
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onResponse: (response: LoginResponseModel?) -> Unit,
        onLoginFailed: (response: LoginFailedResponseModel?) -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                LoginRequestModel(
                    email = email,
                    password = password
                )
            )

            val call: Call<ApiResponseModel<LoginResponseModel>> =
                loginBasicService.loginWithEmailAndPasswordFromApi(
                    endpoint = BuildConfig.LOGIN_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<LoginResponseModel>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<LoginResponseModel>? = response.body()

                when (responseCode) {
                    200 -> {
                        onResponse(responseBody?.body)
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    onBadRequest()
                }

                401 -> {
                    val errorResponse: String? = e.response()?.errorBody()?.string()

                    val loginFailedType =
                        object : TypeToken<ApiResponseModel<LoginFailedResponseModel>>() {}.type

                    val loginFailedResponse: ApiResponseModel<LoginFailedResponseModel>? =
                        gson.fromJson(errorResponse, loginFailedType)

                    if (loginFailedResponse != null) {
                        onLoginFailed(loginFailedResponse.body)
                    }
                }

                else -> {
                    onFailure()
                }
            }
        } catch (e: IOException) {
            onFailure()
        }
    }

    override fun loginWithRefreshToken(
        onResponse: (response: LoginResponseModel?) -> Unit,
        onLoginFailed: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val call: Call<ApiResponseModel<LoginResponseModel>> =
                loginRefreshService.loginWithRefreshTokenFromApi(
                    endpoint = BuildConfig.REFRESH_LOGIN_ENDPOINT
                )

            val response: Response<ApiResponseModel<LoginResponseModel>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<LoginResponseModel>? = response.body()

                when (responseCode) {
                    200 -> {
                        onResponse(responseBody?.body)
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    onLoginFailed()
                }

                401 -> {
                    onLoginFailed()
                }

                else -> {
                    onFailure()
                }
            }
        } catch (e: IOException) {
            onFailure()
        }
    }

    override fun logout(
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val call: Call<Unit> =
                loginAccessService.logoutFromApi(
                    endpoint = BuildConfig.LOGOUT_ENDPOINT
                )

            val response: Response<Unit> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: Unit? = response.body()

                when (responseCode) {
                    204 -> {
                        onResponse()
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> {
                    onResponseFailed()
                }

                else -> {
                    onFailure()
                }
            }
        } catch (e: IOException) {
            onFailure()
        }
    }

    override fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onResponse: (response: LoginResponseModel?) -> Unit,
        onRegisterFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                RegisterRequestModel(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
            )

            val call: Call<ApiResponseModel<LoginResponseModel>> =
                loginBasicService.registerWithEmailAndPasswordFromApi(
                    endpoint = BuildConfig.REGISTER_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<LoginResponseModel>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<LoginResponseModel>? = response.body()

                when (responseCode) {
                    200 -> {
                        onResponse(responseBody?.body)
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    onBadRequest()
                }

                401 -> {
                    onRegisterFailed()
                }

                else -> {
                    onFailure()
                }
            }
        } catch (e: IOException) {
            onFailure()
        }
    }
}