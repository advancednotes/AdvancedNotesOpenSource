package com.advancednotes.data.api.repositories.impl

import com.advancednotes.BuildConfig
import com.advancednotes.data.api.models.UserFirstNameRequestModel
import com.advancednotes.data.api.models.UserLastNameRequestModel
import com.advancednotes.data.api.models.UserPasswordRequestModel
import com.advancednotes.data.api.repositories.ApiUserRepository
import com.advancednotes.data.api.services.UserService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ApiUserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val gson: Gson
) : ApiUserRepository {

    override fun updateFirstName(
        firstName: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                UserFirstNameRequestModel(
                    firstName = firstName
                )
            )

            val call: Call<Void> =
                userService.updateFirstName(
                    endpoint = BuildConfig.USER_FIRST_NAME_ENDPOINT,
                    data = data
                )

            val response: Response<Void> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()

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
                400 -> {
                    onBadRequest()
                }

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

    override fun updateLastName(
        lastName: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                UserLastNameRequestModel(
                    lastName = lastName
                )
            )

            val call: Call<Void> =
                userService.updateLastName(
                    endpoint = BuildConfig.USER_LAST_NAME_ENDPOINT,
                    data = data
                )

            val response: Response<Void> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()

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
                400 -> {
                    onBadRequest()
                }

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

    override fun updatePassword(
        password: String,
        onResponse: () -> Unit,
        onResponseFailed: () -> Unit,
        onBadRequest: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val data: String = gson.toJson(
                UserPasswordRequestModel(
                    password = password
                )
            )

            val call: Call<Void> =
                userService.updatePassword(
                    endpoint = BuildConfig.USER_PASSWORD_ENDPOINT,
                    data = data
                )

            val response: Response<Void> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()

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
                400 -> {
                    onBadRequest()
                }

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

}