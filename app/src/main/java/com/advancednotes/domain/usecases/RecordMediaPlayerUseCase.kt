package com.advancednotes.domain.usecases

import android.net.Uri

interface RecordMediaPlayerUseCase {

    fun isPlaying(): Boolean

    fun playRecord(
        fileUri: Uri,
        progress: Float,
        onStart: (() -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onProgress: ((currentProgress: Float) -> Unit)? = null
    )

    fun stopRecord(onStop: (() -> Unit)? = null)

    fun seekTo(progress: Float)

    fun getDurationMills(uri: Uri): Int
    fun formatProgress(progressMills: Float): String
}