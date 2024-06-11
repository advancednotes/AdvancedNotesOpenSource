package com.advancednotes.utils.files

import android.content.Context
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

object FileHelper {

    fun getCacheFilesDirectory(context: Context): File {
        val directory = File(context.cacheDir, "cache_files")

        if (!directory.exists()) {
            directory.mkdir()
        }

        return directory
    }

    fun cleanCacheFilesDirectory(context: Context) {
        val directory = File(context.cacheDir, "cache_files")

        if (directory.exists()) {
            directory.deleteRecursively()
        }
    }

    fun getMimeType(fileName: String): String {
        val extension: String = getFileExtension(fileName)

        if (extension.isNotEmpty()) {
            val mimeType: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

            if (mimeType != null) {
                return mimeType
            }
        }

        return "application/octet-stream"
    }

    fun getMediaType(fileName: String): MediaType? {
        val extension: String = getFileExtension(fileName)

        if (extension.isNotEmpty()) {
            val mimeType: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

            if (mimeType != null) {
                return mimeType.toMediaTypeOrNull()
            }
        }

        return "application/octet-stream".toMediaTypeOrNull()
    }


    private fun getFileExtension(fileName: String): String {
        val lastDotIndex: Int = fileName.lastIndexOf(".")

        if (lastDotIndex >= 0 && lastDotIndex < fileName.length - 1) {
            return fileName.substring(lastDotIndex + 1).lowercase()
        }

        return ""
    }
}