package com.advancednotes.data.mediastore

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.advancednotes.utils.files.FileHelper.getMimeType
import com.advancednotes.utils.inputstream.copyToWithProgress
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class MediaStoreHelper @Inject constructor(
    private val context: Context
) {

    fun saveFileToDownloads(
        file: File,
        onProgress: ((progress: Int) -> Unit)? = null
    ) {
        val fileName: String = file.name

        val mimeType: String = getMimeType(fileName)
        val downloadsDirectory: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs()
        }

        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        lateinit var outputStream: FileOutputStream
        lateinit var inputStream: FileInputStream

        try {
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                val fileAux = File(downloadsDirectory, fileName)
                fileAux.absolutePath.toUri()
            }

            uri?.let {
                outputStream = resolver.openOutputStream(it) as FileOutputStream
                inputStream = FileInputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyToWithProgress(
                            out = output,
                            progressCallback = { bytesCopied ->
                                onProgress?.invoke(
                                    calculateProgress(
                                        bytesCopied,
                                        file.length()
                                    )
                                )
                            }
                        )
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream.close()
            inputStream.close()
        }
    }

    private fun calculateProgress(bytesCopied: Long, totalBytes: Long): Int {
        if (totalBytes <= 0) return 0

        val progress = ((bytesCopied.toFloat() / totalBytes.toFloat()) * 100).toInt()
        return progress.coerceIn(0, 100)
    }
}