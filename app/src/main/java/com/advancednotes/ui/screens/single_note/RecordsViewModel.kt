package com.advancednotes.ui.screens.single_note

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.RecordMediaPlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val recordMediaPlayerUseCase: RecordMediaPlayerUseCase,
    private val notesFilesUseCase: NotesFilesUseCase
) : ViewModel() {

    private val _recordPlayingIdentifier = MutableStateFlow("")
    val recordPlayingIdentifier: StateFlow<String> = _recordPlayingIdentifier.asStateFlow()

    fun setRecordPlayingIdentifier(identifier: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _recordPlayingIdentifier.value = identifier
        }
    }

    fun isPlaying(): Boolean = recordMediaPlayerUseCase.isPlaying()

    fun isTheSameRecord(identifier: String): Boolean = identifier == _recordPlayingIdentifier.value

    fun playRecord(
        fileUri: Uri,
        progress: Float,
        onStart: (() -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onProgress: ((currentProgress: Float) -> Unit)? = null
    ) {
        recordMediaPlayerUseCase.playRecord(
            fileUri = fileUri,
            progress = progress,
            onStart = onStart,
            onComplete = onComplete,
            onProgress = onProgress
        )
    }

    fun stopRecord(
        onStop: (() -> Unit)? = null
    ) {
        recordMediaPlayerUseCase.stopRecord(onStop = onStop)
    }

    fun seekTo(progress: Float) {
        recordMediaPlayerUseCase.seekTo(progress = progress)
    }

    fun getDurationMills(
        uri: Uri
    ): Int {
        return recordMediaPlayerUseCase.getDurationMills(uri = uri)
    }

    fun formatProgress(progressMills: Float): String {
        return recordMediaPlayerUseCase.formatProgress(progressMills = progressMills)
    }

    fun getFileUri(context: Context, noteFile: NoteFile): Uri? {
        return notesFilesUseCase.getFileUriFromNoteFile(context, noteFile)
    }
}