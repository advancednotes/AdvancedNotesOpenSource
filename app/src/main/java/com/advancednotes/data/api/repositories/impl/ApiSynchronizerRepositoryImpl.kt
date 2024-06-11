package com.advancednotes.data.api.repositories.impl

import com.advancednotes.BuildConfig
import com.advancednotes.data.api.models.ApiResponseModel
import com.advancednotes.data.api.models.NoteFileModel
import com.advancednotes.data.api.models.NoteModel
import com.advancednotes.data.api.models.SynchronizeNoteFileRequestModel
import com.advancednotes.data.api.models.SynchronizeNoteFileResponseModel
import com.advancednotes.data.api.models.SynchronizeNoteRequestModel
import com.advancednotes.data.api.models.SynchronizeNoteResponseModel
import com.advancednotes.data.api.models.SynchronizeTagRequestModel
import com.advancednotes.data.api.models.SynchronizeTagResponseModel
import com.advancednotes.data.api.models.TagModel
import com.advancednotes.data.api.repositories.ApiSynchronizerRepository
import com.advancednotes.data.api.services.SynchronizerService
import com.advancednotes.utils.files.FileHelper.getMediaType
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ApiSynchronizerRepositoryImpl @Inject constructor(
    private val synchronizerService: SynchronizerService,
    private val gson: Gson
) : ApiSynchronizerRepository {

    override fun synchronizeNote(
        noteUUID: String,
        noteToSynchronize: NoteModel?,
        onResponse: (response: SynchronizeNoteResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                SynchronizeNoteRequestModel(
                    noteUUID = noteUUID,
                    noteToSynchronize = noteToSynchronize
                )
            )

            val call: Call<ApiResponseModel<SynchronizeNoteResponseModel>> =
                synchronizerService.syncNoteFromApi(
                    endpoint = BuildConfig.SYNCHRONIZER_NOTE_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<SynchronizeNoteResponseModel>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<SynchronizeNoteResponseModel>? = response.body()

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

    override fun synchronizeNoteFile(
        noteFileUUID: String,
        noteFileToSynchronize: NoteFileModel?,
        fileToSynchronize: File?,
        onResponse: (response: SynchronizeNoteFileResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                SynchronizeNoteFileRequestModel(
                    noteFileUUID = noteFileUUID,
                    noteFileToSynchronize = noteFileToSynchronize
                )
            )

            val requestBody: RequestBody = if (
                noteFileToSynchronize != null &&
                fileToSynchronize != null
            ) {
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        name = "data",
                        value = data
                    )
                    .addFormDataPart(
                        name = "file",
                        filename = fileToSynchronize.name,
                        body = fileToSynchronize.asRequestBody(
                            getMediaType(
                                fileToSynchronize.name
                            )
                        )
                    )
                    .build()
            } else {
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        name = "data",
                        value = data
                    )
                    .build()
            }

            val call: Call<ApiResponseModel<SynchronizeNoteFileResponseModel?>> =
                synchronizerService.syncNoteFileFromApi(
                    endpoint = BuildConfig.SYNCHRONIZER_FILE_ENDPOINT,
                    requestBody = requestBody
                )

            val response: Response<ApiResponseModel<SynchronizeNoteFileResponseModel?>> =
                call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<SynchronizeNoteFileResponseModel?>? =
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

    override fun synchronizeTag(
        tagUUID: String,
        tagToSynchronize: TagModel?,
        onResponse: (response: SynchronizeTagResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)?,
        onAccessTokenNotValid: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        try {
            val data: String = gson.toJson(
                SynchronizeTagRequestModel(
                    tagUUID = tagUUID,
                    tagToSynchronize = tagToSynchronize
                )
            )

            val call: Call<ApiResponseModel<SynchronizeTagResponseModel?>> =
                synchronizerService.syncTagFromApi(
                    endpoint = BuildConfig.SYNCHRONIZER_TAG_ENDPOINT,
                    data = data
                )

            val response: Response<ApiResponseModel<SynchronizeTagResponseModel?>> = call.execute()

            if (response.isSuccessful) {
                val responseCode: Int = response.code()
                val responseBody: ApiResponseModel<SynchronizeTagResponseModel?>? = response.body()

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