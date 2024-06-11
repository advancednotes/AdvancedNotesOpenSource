package com.advancednotes.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.advancednotes.domain.usecases.PreSynchronizerUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SynchronizeWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val preSynchronizerUseCase: PreSynchronizerUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {

            try {
                preSynchronizerUseCase.preSynchronizeNotes()
                preSynchronizerUseCase.preSynchronizeTags()
                preSynchronizerUseCase.preSynchronizeNotesFiles()

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}