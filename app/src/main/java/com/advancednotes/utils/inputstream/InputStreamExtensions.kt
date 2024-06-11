package com.advancednotes.utils.inputstream

import java.io.InputStream
import java.io.OutputStream

fun InputStream.copyToWithProgress(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    progressCallback: ((bytesCopied: Long) -> Unit)? = null
): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)

    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        progressCallback?.invoke(bytesCopied)
        bytes = read(buffer)
    }

    return bytesCopied
}