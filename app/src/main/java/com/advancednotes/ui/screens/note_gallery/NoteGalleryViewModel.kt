package com.advancednotes.ui.screens.note_gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.common.Constants
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.utils.services.DownloadFileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NoteGalleryViewModel @Inject constructor(
    private val notesFilesUseCase: NotesFilesUseCase
) : ViewModel() {

    private val _noteGalleryState = MutableStateFlow(NoteGalleryState())
    val noteGalleryState: StateFlow<NoteGalleryState> = _noteGalleryState.asStateFlow()

    val exitChannel = Channel<Unit>(Channel.CONFLATED)

    fun getNoteFile(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteFile: NoteFile = notesFilesUseCase.getNoteFileByUUID(uuid)

            _noteGalleryState.value =
                _noteGalleryState.value.copy(
                    noteFile = noteFile
                )
        }
    }

    fun getPathFromNoteFile(noteFile: NoteFile): String? {
        val file: File? = notesFilesUseCase.getFile(noteFile)

        return file?.absolutePath
    }

    fun downloadImage(context: Context, noteFile: NoteFile) {
        val extras: Bundle = bundleOf(
            "noteFileUUID" to noteFile.uuid
        )

        val mIntent: Intent = Intent(context, DownloadFileService::class.java)
            .setAction(Constants.ACTION_START_SERVICE)
            .putExtras(extras)

        context.startService(mIntent)
    }

    fun deleteGalleryImage() {
        viewModelScope.launch(Dispatchers.IO) {
            notesFilesUseCase.updateNoteFileDeletedPermanently(_noteGalleryState.value.noteFile.uuid)

            exit()
        }
    }

    private fun exit() {
        viewModelScope.launch(Dispatchers.Default) {
            exitChannel.trySend(Unit)
        }
    }
}