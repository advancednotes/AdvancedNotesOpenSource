package com.advancednotes.utils.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerManager @Inject constructor(
    private val context: Context
) {
    fun launchSynchronizeWorker() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            SynchronizeWorker::class.java,
            24,
            TimeUnit.HOURS
        )
            .addTag("synchronizeWorker")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                "synchronizeWorker",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )
    }

    fun launchDeleteWorker() {
        val constraints: Constraints = Constraints.Builder()
            .build()

        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            DeleteNotesWorker::class.java,
            24,
            TimeUnit.HOURS
        )
            .addTag("deleteWorker")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                "deleteWorker",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )
    }
}