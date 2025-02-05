package dev.atick.data.utils

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    /**
     * Flow that emits a boolean value indicating whether the sync operation is in progress.
     */
    val isSyncing: Flow<Boolean>

    /**
     * Starts the sync operation.
     */
    fun requestSync()
}

/**
 * Interface representing a syncable entity.
 */
interface Syncable {
    /**
     * Synchronizes data and returns a Flow emitting the progress of the sync operation.
     *
     * @return A Flow emitting SyncProgress objects representing the progress of the sync operation.
     */
    suspend fun sync(): Flow<SyncProgress>
}

/**
 * Data class that represents the progress of a sync operation.
 *
 * @param total The total number of items to sync.
 * @param current The current number of items synced.
 * @param message The message to display during the sync operation.
 */
data class SyncProgress(
    val total: Int = 0,
    val current: Int = 0,
    val message: String? = null,
)