/*
 * Copyright 2025 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.atick.core.di.IoDispatcher
import dev.atick.data.repository.home.HomeRepository
import dev.atick.sync.extensions.syncForegroundInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Worker to sync data.
 *
 * @param context The [Context].
 * @param workerParameters The [WorkerParameters].
 * @param ioDispatcher The [CoroutineDispatcher] for I/O operations.
 * @param homeRepository The [HomeRepository].
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
) : CoroutineWorker(context, workerParameters) {

    /**
     * Provides the foreground information for the worker.
     * @return The foreground information.
     */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(0, 0)
    }

    /**
     * Provides the foreground information for the worker.
     * @param total The total number of items.
     * @param current The current number of items.
     * @return The foreground information.
     */
    private fun getForegroundInfo(total: Int, current: Int): ForegroundInfo {
        return context.syncForegroundInfo(total, current)
    }

    /**
     * Performs the work to sync data.
     * @return The result of the work.
     */
    override suspend fun doWork(): Result {
        return withContext(ioDispatcher) {
            try {
                setForeground(getForegroundInfo())
                homeRepository.sync()
                    .flowOn(ioDispatcher)
                    .collect { progress ->
                        Timber.d("SyncWorker: Progress: $progress")
                        setForeground(
                            foregroundInfo = getForegroundInfo(
                                total = progress.total,
                                current = progress.current,
                            ),
                        )
                    }
                Result.success()
            } catch (e: Exception) {
                if (runAttemptCount < TOTAL_SYNC_ATTEMPTS) {
                    Timber.d(e, "Error syncing, retrying ($runAttemptCount/$TOTAL_SYNC_ATTEMPTS)")
                    Result.retry()
                } else {
                    Timber.e(e, "Error syncing")
                    Result.failure()
                }
            }
        }
    }

    companion object {
        /**
         * The total number of sync attempts.
         */
        const val TOTAL_SYNC_ATTEMPTS = 3

        /**
         * Network constraints for syncing data.
         */
        val SyncConstraints
            get() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        /**
         * Starts the work to sync data.
         * @return The one-time work request to sync data.
         */
        fun startUpSyncWork(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<DelegatingWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(SyncConstraints)
                .setInputData(SyncWorker::class.delegatedData())
                .build()
        }
    }
}
