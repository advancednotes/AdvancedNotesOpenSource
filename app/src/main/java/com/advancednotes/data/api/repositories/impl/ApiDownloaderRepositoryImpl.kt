package com.advancednotes.data.api.repositories.impl

import android.content.Context
import com.advancednotes.BuildConfig
import com.advancednotes.data.api.repositories.ApiDownloaderRepository
import com.advancednotes.data.api.services.DownloaderService
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.utils.date.DateHelper.getCurrentDateForFileName
import com.advancednotes.utils.files.FileHelper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ApiDownloaderRepositoryImpl @Inject constructor(
    private val context: Context,
    private val downloaderService: DownloaderService,
) : ApiDownloaderRepository {

    override fun downloadFile(
        noteFile: NoteFile,
        noteFileURIEndpoint: String
    ): File? {
        try {
            val call: Call<ResponseBody> =
                downloaderService.downloadFile(
                    endpoint = "${BuildConfig.FILES_ENDPOINT}/$noteFileURIEndpoint"
                )

            val response: Response<ResponseBody> = call.execute()

            if (response.isSuccessful) {
                val responseBody: ResponseBody? = response.body()

                if (responseBody != null) {
                    val fileName = "temp_${getCurrentDateForFileName()}"
                    val directory: File = FileHelper.getCacheFilesDirectory(context)
                    val file: File =
                        File.createTempFile(fileName, ".${noteFile.fileExtension}", directory)

                    val inputStream: InputStream = responseBody.byteStream()
                    val outputStream = FileOutputStream(file)

                    inputStream.use {
                        it.copyTo(outputStream)
                    }

                    return file
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            return null
        } catch (e: IOException) {
            return null
        }

        return null
    }
}