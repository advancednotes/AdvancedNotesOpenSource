package com.advancednotes.data.api.repositories.impl

import com.advancednotes.BuildConfig
import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.PreSynchronizeItem
import com.advancednotes.data.api.models.PreSynchronizeNotesFilesRequestModel
import com.advancednotes.data.api.models.PreSynchronizeNotesFilesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeNotesRequestModel
import com.advancednotes.data.api.models.PreSynchronizeNotesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeTagsRequestModel
import com.advancednotes.data.api.models.PreSynchronizeTagsResponseModel
import com.advancednotes.data.api.repositories.ApiPreSynchronizerRepository
import com.advancednotes.data.api.services.PreSynchronizerService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ApiPreSynchronizerRepositoryImpl @Inject constructor(
    private val preSynchronizerService: PreSynchronizerService,
    private val gson: Gson
) : ApiPreSynchronizerRepository {

    override fun preSynchronizeNotes(
        notesToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeNotesResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                PreSynchronizeNotesRequestModel(
                    preSynchronizeItems = notesToPreSynchronize
                )
            )

            val call: Call<ApiResponseModel<PreSynchronizeNotesResponseModel>> =
                preSynchronizerService.preSyncNotesFromApi(
                    endpoint = BuildConfig.PRE_SYNCHRONIZER_NOTES_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<PreSynchronizeNotesResponseModel>> =
                call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<PreSynchronizeNotesResponseModel>? =
                    response.body()

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
                    onBadRequest?.invoke()
                }

                401 -> {
                    onAccessTokenNotValid?.invoke()
                }

                else -> {
                    onFailure?.invoke()
                }
            }
        } catch (e: IOException) {
            onFailure?.invoke()
        }
    }

    override fun preSynchronizeNotesFiles(
        notesFilesToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeNotesFilesResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                PreSynchronizeNotesFilesRequestModel(
                    preSynchronizeItems = notesFilesToPreSynchronize
                )
            )

            val call: Call<ApiResponseModel<PreSynchronizeNotesFilesResponseModel>> =
                preSynchronizerService.preSyncNotesFilesFromApi(
                    endpoint = BuildConfig.PRE_SYNCHRONIZER_FILES_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<PreSynchronizeNotesFilesResponseModel>> =
                call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<PreSynchronizeNotesFilesResponseModel>? =
                    response.body()

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
                    onBadRequest?.invoke()
                }

                401 -> {
                    onAccessTokenNotValid?.invoke()
                }

                else -> {
                    onFailure?.invoke()
                }
            }
        } catch (e: IOException) {
            onFailure?.invoke()
        }
    }

    override fun preSynchronizeTags(
        tagsToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeTagsResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                PreSynchronizeTagsRequestModel(
                    preSynchronizeItems = tagsToPreSynchronize
                )
            )

            val call: Call<ApiResponseModel<PreSynchronizeTagsResponseModel>> =
                preSynchronizerService.preSyncTagsFromApi(
                    endpoint = BuildConfig.PRE_SYNCHRONIZER_TAGS_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<PreSynchronizeTagsResponseModel>> =
                call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<PreSynchronizeTagsResponseModel>? =
                    response.body()

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
                    onBadRequest?.invoke()
                }

                401 -> {
                    onAccessTokenNotValid?.invoke()
                }

                else -> {
                    onFailure?.invoke()
                }
            }
        } catch (e: IOException) {
            onFailure?.invoke()
        }
    }
}