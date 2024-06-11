package com.advancednotes.domain.usecases.impl

import android.content.Context
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.api.models.toDomain
import com.advancednotes.data.api.repositories.ApiDownloaderRepository
import com.advancednotes.data.api.repositories.ApiSynchronizerRepository
import com.advancednotes.data.datastore.saveLastSyncNotesDate
import com.advancednotes.data.datastore.saveLastSyncNotesFilesDate
import com.advancednotes.data.datastore.saveLastSyncTagsDate
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.models.toApi
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.SynchronizerUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import java.io.File
import javax.inject.Inject

class SynchronizerUseCaseImpl @Inject constructor(
    private val context: Context,
    private val apiSynchronizerRepository: ApiSynchronizerRepository,
    private val apiDownloaderRepository: ApiDownloaderRepository,
    private val authenticationUseCase: AuthenticationUseCase,
    private val notesUseCase: NotesUseCase,
    private val notesFilesUseCase: NotesFilesUseCase,
    private val tagsUseCase: TagsUseCase
) : SynchronizerUseCase {

    override fun synchronizeNotes(
        notesUUIDs: List<String>,
        onComplete: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            notesUUIDs.forEach { noteUUID ->
                val note: Note = notesUseCase.getNoteByUUID(noteUUID)

                apiSynchronizerRepository.synchronizeNote(
                    noteUUID = noteUUID,
                    noteToSynchronize = if (note.uuid != EMPTY_UUID) {
                        note.toApi()
                    } else null,
                    onResponse = { response ->
                        if (response?.noteToSynchronizeFromApi != null) {
                            val noteExistsInDatabase: Boolean = note.uuid != EMPTY_UUID

                            if (noteExistsInDatabase) {
                                val noteToSynchronizeFromApiWithId =
                                    response.noteToSynchronizeFromApi.copy(
                                        id = note.id
                                    )

                                notesUseCase.updateNote(
                                    note = noteToSynchronizeFromApiWithId.toDomain(),
                                    fromSynchronize = true
                                )
                            } else {
                                notesUseCase.insertNote(response.noteToSynchronizeFromApi.toDomain())
                            }
                        }
                    },
                    onAccessTokenNotValid = {
                        authenticationUseCase.loginWithRefreshToken(
                            onLoginSuccess = {
                                synchronizeNotes(
                                    notesUUIDs = notesUUIDs,
                                    onComplete = onComplete
                                )
                            }
                        )
                    }
                )
            }

            context.saveLastSyncNotesDate()
        }

        onComplete?.invoke()
    }

    override fun synchronizeNotesFiles(
        notesFilesUUIDs: List<String>,
        onComplete: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            notesFilesUUIDs.forEach { noteFileUUID ->
                val noteFile: NoteFile = notesFilesUseCase.getNoteFileByUUID(noteFileUUID)
                val file: File? = if (noteFile.uuid != EMPTY_UUID) {
                    notesFilesUseCase.getFile(noteFile)
                } else null

                apiSynchronizerRepository.synchronizeNoteFile(
                    noteFileUUID = noteFileUUID,
                    noteFileToSynchronize = if (noteFile.uuid != EMPTY_UUID) {
                        noteFile.toApi()
                    } else null,
                    fileToSynchronize = file,
                    onResponse = { response ->
                        if (
                            response?.noteFileToSynchronizeFromApi != null &&
                            response.noteFileURIEndpoint != null
                        ) {
                            val downloadedFile: File? = apiDownloaderRepository.downloadFile(
                                noteFile = response.noteFileToSynchronizeFromApi.toDomain(),
                                noteFileURIEndpoint = response.noteFileURIEndpoint
                            )

                            if (downloadedFile != null) {
                                val noteFileExistsInDatabase: Boolean =
                                    noteFile.uuid != EMPTY_UUID

                                if (noteFileExistsInDatabase) {
                                    val noteFileToSynchronizeFromApiWithId =
                                        response.noteFileToSynchronizeFromApi.copy(
                                            id = noteFile.id
                                        )

                                    notesFilesUseCase.updateNoteFile(
                                        noteFile = noteFileToSynchronizeFromApiWithId.toDomain(),
                                        tempFile = downloadedFile
                                    )
                                } else {
                                    notesFilesUseCase.insertNoteFile(
                                        noteFile = response.noteFileToSynchronizeFromApi.toDomain(),
                                        tempFile = downloadedFile
                                    )
                                }
                            }
                        }
                    },
                    onAccessTokenNotValid = {
                        authenticationUseCase.loginWithRefreshToken(
                            onLoginSuccess = {
                                synchronizeNotesFiles(
                                    notesFilesUUIDs = notesFilesUUIDs,
                                    onComplete = onComplete
                                )
                            }
                        )
                    }
                )
            }

            context.saveLastSyncNotesFilesDate()
        }

        onComplete?.invoke()
    }

    override fun synchronizeTags(
        tagsUUIDs: List<String>,
        onComplete: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            tagsUUIDs.forEach { tagUUID ->
                val tag: Tag = tagsUseCase.getTagByUUID(tagUUID)

                apiSynchronizerRepository.synchronizeTag(
                    tagUUID = tagUUID,
                    tagToSynchronize = if (tag.uuid != EMPTY_UUID) {
                        tag.toApi()
                    } else null,
                    onResponse = { response ->
                        if (response?.tagToSynchronizeFromApi != null) {
                            val tagExistsInDatabase: Boolean = tag.uuid != EMPTY_UUID

                            if (tagExistsInDatabase) {
                                val tagToSynchronizeFromApiWithId =
                                    response.tagToSynchronizeFromApi.copy(
                                        id = tag.id
                                    )

                                tagsUseCase.updateTag(
                                    tag = tagToSynchronizeFromApiWithId.toDomain(),
                                    fromSynchronize = true
                                )
                            } else {
                                tagsUseCase.insertTag(response.tagToSynchronizeFromApi.toDomain())
                            }
                        }
                    },
                    onAccessTokenNotValid = {
                        authenticationUseCase.loginWithRefreshToken(
                            onLoginSuccess = {
                                synchronizeTags(
                                    tagsUUIDs = tagsUUIDs,
                                    onComplete = onComplete
                                )
                            }
                        )
                    }
                )
            }
        }

        context.saveLastSyncTagsDate()

        onComplete?.invoke()
    }
}