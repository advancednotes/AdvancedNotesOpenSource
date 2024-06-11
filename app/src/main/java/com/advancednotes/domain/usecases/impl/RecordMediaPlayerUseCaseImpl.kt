package com.advancednotes.domain.usecases.impl

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import com.advancednotes.domain.usecases.RecordMediaPlayerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordMediaPlayerUseCaseImpl @Inject constructor(
    private val context: Context,
) : RecordMediaPlayerUseCase {

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    private var mMediaPlayer: MediaPlayer? = null

    private var stoppedByUser: Boolean = false

    override fun isPlaying(): Boolean = mMediaPlayer?.isPlaying == true

    override fun playRecord(
        fileUri: Uri,
        progress: Float,
        onStart: (() -> Unit)?,
        onComplete: (() -> Unit)?,
        onProgress: ((currentProgress: Float) -> Unit)?
    ) {
        stoppedByUser = false

        mMediaPlayer = MediaPlayer().apply {
            setDataSource(context, fileUri)
            prepare()
        }

        if (progress != 0f) {
            seekTo(progress = progress)
        }

        mMediaPlayer?.setOnCompletionListener {
            if (!stoppedByUser) {
                clearRecord()
                onComplete?.invoke()
            }
        }

        mMediaPlayer?.setOnPreparedListener {
            mMediaPlayer?.start()
            onStart?.invoke()

            coroutineScope.launch {
                while (isPlaying()) {
                    onProgress?.invoke(getCurrentProgressMills())
                    ensureActive()
                }
            }
        }
    }

    override fun stopRecord(onStop: (() -> Unit)?) {
        stoppedByUser = true
        clearRecord()
        onStop?.invoke()
    }

    override fun seekTo(progress: Float) {
        mMediaPlayer?.seekTo(progress.toInt())
    }

    override fun getDurationMills(uri: Uri): Int {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val durationString: String? =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration: Int = durationString?.toIntOrNull() ?: 0

        return duration
    }

    override fun formatProgress(progressMills: Float): String {
        val progressInSeconds: Float = progressMills / 1000

        val progressMinutes: Float = progressInSeconds / 60
        val progressSeconds: Float = progressInSeconds % 60

        return String.format("%02d:%02d", progressMinutes.toInt(), progressSeconds.toInt())
    }

    private fun clearRecord() {
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    private fun getCurrentProgressMills(): Float {
        val currentProgress = mMediaPlayer?.currentPosition ?: 0
        return currentProgress.toFloat()
    }
}