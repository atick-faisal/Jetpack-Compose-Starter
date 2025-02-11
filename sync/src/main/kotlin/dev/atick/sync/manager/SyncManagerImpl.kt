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

package dev.atick.sync.manager

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.data.utils.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of [SyncManager].
 *
 * @param context [Context].
 */
internal class SyncManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SyncManager {
    /**
     * Provides a flow that emits `true` if a sync operation is in progress, `false` otherwise.
     */
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    /**
     * Requests a sync operation.
     */
    override fun requestSync() {
        Timber.d("Requesting sync")
        Sync.initialize(context)
    }
}

/**
 * Checks if any of the [WorkInfo] is running.
 */
private fun List<WorkInfo>.anyRunning() = any { it.state == State.RUNNING }
