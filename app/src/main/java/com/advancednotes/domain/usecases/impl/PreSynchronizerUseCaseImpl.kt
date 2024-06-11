package com.advancednotes.domain.usecases.impl

import com.advancednotes.data.api.models.NoteFileModel
import com.advancednotes.data.api.models.NoteModel
import com.advancednotes.data.api.models.PreSynchronizeItem
import com.advancednotes.data.api.models.TagModel
import com.advancednotes.data.api.repositories.ApiPreSynchronizerRepository
import com.advancednotes.domain.models.toApi
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.PreSynchronizerUseCase
import com.advancednotes.domain.usecases.SynchronizerUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import javax.inject.Inject

class PreSynchronizerUseCaseImpl @Inject constructor(
    private val apiPreSynchronizerRepository: ApiPreSynchronizerRepository,
    private val synchronizerUseCase: SynchronizerUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val notesUseCase: NotesUseCase,
    private val notesFilesUseCase: NotesFilesUseCase,
    private val tagsUseCase: TagsUseCase
) : PreSynchronizerUseCase {

    override fun preSynchronizeNotes(
        onComplete: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            val notesToPreSynchronize: List<PreSynchronizeItem> =
                notesUseCase.getAllNotes().map {
                    val noteToApi: NoteModel = it.toApi()

                    PreSynchronizeItem(
                        uuid = noteToApi.uuid,
                        modificationDate = noteToApi.modificationDate
                    )
                }

            apiPreSynchronizerRepository.preSynchronizeNotes(
                notesToPreSynchronize = notesToPreSynchronize,
                onResponse = { response ->
                    if (response != null) {
                        synchronizerUseCase.synchronizeNotes(
                            notesUUIDs = response.notesToPreSynchronizeUUIDs,
                            onComplete = onComplete
                        )
                    } else {
                        onComplete?.invoke()
                    }
                },
                onBadRequest = {
                    onFailure?.invoke()
                },
                onAccessTokenNotValid = {
                    authenticationUseCase.loginWithRefreshToken(
                        onLoginSuccess = {
                            preSynchronizeNotes()
                        },
                        onFailure = onFailure
                    )
                },
                onFailure = {
                    onFailure?.invoke()
                }
            )
        }
    }

    override fun preSynchronizeNotesFiles(
        onComplete: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            val notesFilesToPreSynchronize: List<PreSynchronizeItem> =
                notesFilesUseCase.getAllNotesFiles().map {
                    val noteFileToApi: NoteFileModel = it.toApi()

                    PreSynchronizeItem(
                        uuid = noteFileToApi.uuid,
                        modificationDate = noteFileToApi.modificationDate
                    )
                }

            apiPreSynchronizerRepository.preSynchronizeNotesFiles(
                notesFilesToPreSynchronize = notesFilesToPreSynchronize,
                onResponse = { response ->
                    if (response != null) {
                        synchronizerUseCase.synchronizeNotesFiles(
                            notesFilesUUIDs = response.notesFilesToPreSynchronizeUUIDs,
                            onComplete = onComplete
                        )
                    } else {
                        onComplete?.invoke()
                    }
                },
                onBadRequest = {
                    onFailure?.invoke()
                },
                onAccessTokenNotValid = {
                    authenticationUseCase.loginWithRefreshToken(
                        onLoginSuccess = {
                            preSynchronizeNotesFiles()
                        },
                        onFailure = onFailure
                    )
                },
                onFailure = {
                    onFailure?.invoke()
                }
            )
        }
    }

    override fun preSynchronizeTags(
        onComplete: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        if (authenticationUseCase.hasAccessToken()) {
            val tagsToPreSynchronize: List<PreSynchronizeItem> =
                tagsUseCase.getTags().map {
                    val tagToApi: TagModel = it.toApi()

                    PreSynchronizeItem(
                        uuid = tagToApi.uuid,
                        modificationDate = tagToApi.modificationDate
                    )
                }

            apiPreSynchronizerRepository.preSynchronizeTags(
                tagsToPreSynchronize = tagsToPreSynchronize,
                onResponse = { response ->
                    if (response != null) {
                        synchronizerUseCase.synchronizeTags(
                            tagsUUIDs = response.tagsToPreSynchronizeUUIDs,
                            onComplete = onComplete
                        )
                    } else {
                        onComplete?.invoke()
                    }
                },
                onBadRequest = {
                    onFailure?.invoke()
                },
                onAccessTokenNotValid = {
                    authenticationUseCase.loginWithRefreshToken(
                        onLoginSuccess = {
                            preSynchronizeTags()
                        },
                        onFailure = onFailure
                    )
                },
                onFailure = {
                    onFailure?.invoke()
                }
            )
        }
    }
}