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
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

/**
 * An entry point to retrieve the [HiltWorkerFactory] at runtime
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryEntryPoint {
    fun hiltWorkerFactory(): HiltWorkerFactory
}

private const val WORKER_CLASS_NAME = "JetpackWorkerClassName"

/**
 * Adds metadata to a WorkRequest to identify what [CoroutineWorker] the [DelegatingWorker] should
 * delegate to
 *
 * @param inputData a list of key-value pairs to be added to the WorkRequest's input data
 *
 * @return a [Data] object with the worker class name and any additional input data
 */
internal fun KClass<out CoroutineWorker>.delegatedData(
    inputData: List<Pair<String, Any>> = emptyList(),
) =
    Data.Builder()
        .putString(WORKER_CLASS_NAME, qualifiedName)
        .apply {
            inputData.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putDouble(key, value)
                    else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
                }
            }
        }
        .build()

/**
 * A worker that delegates sync to another [CoroutineWorker] constructed with a [HiltWorkerFactory].
 *
 * This allows for creating and using [CoroutineWorker] instances with extended arguments
 * without having to provide a custom WorkManager configuration that the app module needs to utilize.
 *
 * In other words, it allows for custom workers in a library module without having to own
 * configuration of the WorkManager singleton.
 */
class DelegatingWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    /**
     * The class name of the worker to delegate to
     */
    private val workerClassName =
        workerParams.inputData.getString(WORKER_CLASS_NAME) ?: ""

    /**
     * The worker to delegate to constructed with a [HiltWorkerFactory]
     */
    private val delegateWorker =
        EntryPointAccessors.fromApplication<HiltWorkerFactoryEntryPoint>(appContext)
            .hiltWorkerFactory()
            .createWorker(appContext, workerClassName, workerParams)
            as? CoroutineWorker
            ?: throw IllegalArgumentException("Unable to find appropriate worker")

    /**
     * Retrieves the foreground info from the delegate worker
     */
    override suspend fun getForegroundInfo(): ForegroundInfo =
        delegateWorker.getForegroundInfo()

    /**
     * Delegates the work to the delegate worker
     */
    override suspend fun doWork(): Result =
        delegateWorker.doWork()
}
