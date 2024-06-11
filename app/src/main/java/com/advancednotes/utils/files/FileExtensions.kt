package com.advancednotes.utils.files

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

fun File.toThumbnailImageBitmap(): ImageBitmap {
    val bitmap: Bitmap = BitmapFactory.decodeFile(absolutePath)

    val thumbnailSize = 256
    val thumbnail: ImageBitmap =
        ThumbnailUtils.extractThumbnail(bitmap, thumbnailSize, thumbnailSize).asImageBitmap()

    bitmap.recycle()

    return thumbnail
}

fun File.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeFile(absolutePath).asImageBitmap()
}