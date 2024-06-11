package com.advancednotes.utils.bitmap

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toFile(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): File {
    val file: File = File.createTempFile("image", ".$format")
    val outputStream = FileOutputStream(file)
    compress(format, quality, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}