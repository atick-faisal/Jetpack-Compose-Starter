package dev.atick.data.repository.home

import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.room.data.LocalDataSource
import dev.atick.core.room.models.SyncAction
import dev.atick.core.utils.suspendRunCatching
import dev.atick.data.models.home.Jetpack
import dev.atick.data.models.home.mapToJetpacks
import dev.atick.data.models.home.toFirebaseJetpack
import dev.atick.data.models.home.toJetpack
import dev.atick.data.models.home.toJetpackEntity
import dev.atick.data.utils.SyncManager
import dev.atick.data.utils.SyncProgress
import dev.atick.firebase.firestore.data.FirebaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val preferencesDataSource: UserPreferencesDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val syncManager: SyncManager,
) : HomeRepository {

    // TODO: Implement userId caching that respects sign out

    override fun getJetpacks(): Flow<List<Jetpack>> {
        // Request a sync when fetching jetpacks
        // TODO: This should be done in a more efficient way
        syncManager.requestSync()

        return flow {
            val userId = preferencesDataSource.getUserIdOrThrow()
            val jetpacks = localDataSource.getJetpacks(userId).map { it.mapToJetpacks() }
            emitAll(jetpacks)
        }
    }

    override fun getJetpack(id: String): Flow<Jetpack> {
        return localDataSource.getJetpack(id).map { it.toJetpack() }
    }

    override suspend fun createOrUpdateJetpack(jetpack: Jetpack): Result<Unit> {
        val userId = preferencesDataSource.getUserIdOrThrow()
        return suspendRunCatching {
            localDataSource.upsertJetpack(
                jetpack
                    .toJetpackEntity()
                    .copy(
                        userId = userId,
                        lastUpdated = System.currentTimeMillis(),
                        needsSync = true,
                        syncAction = SyncAction.UPSERT,
                    ),
            )
            syncManager.requestSync()
        }
    }

    override suspend fun markJetpackAsDeleted(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.markJetpackAsDeleted(jetpack.id)
            syncManager.requestSync()
        }
    }

    override suspend fun sync(): Flow<SyncProgress> {
        return flow {
            val userId = preferencesDataSource.getUserIdOrThrow()
            val lastSynced = localDataSource.getLatestUpdateTimestamp(userId)
            val remoteJetpacks = firebaseDataSource.pull(userId, lastSynced)
            val totalRemoteJetpacks = remoteJetpacks.size

            Timber.d("Syncing $totalRemoteJetpacks remote jetpacks")

            val unsyncedJetpacks = localDataSource.getUnsyncedJetpacks(userId)
            val totalUnsyncedJetpacks = unsyncedJetpacks.size

            Timber.d("Syncing $totalUnsyncedJetpacks unsynced jetpacks")

            val totalSync = totalRemoteJetpacks + totalUnsyncedJetpacks

            Timber.d("Total sync: $totalSync")

            // Pull updates from remote
            remoteJetpacks.forEachIndexed { index, remoteJetpack ->
                localDataSource.upsertJetpack(remoteJetpack.toJetpackEntity())
                emit(SyncProgress(total = totalSync, current = index + 1))
            }

            // Push updates to remote
            unsyncedJetpacks.forEachIndexed { index, unsyncedJetpack ->
                when (unsyncedJetpack.syncAction) {
                    SyncAction.UPSERT -> {
                        Timber.d("Syncing create/update jetpack: ${unsyncedJetpack.id}")
                        firebaseDataSource.createOrUpdate(
                            unsyncedJetpack
                                .toFirebaseJetpack()
                                .copy(
                                    lastSynced = System.currentTimeMillis(),
                                ),
                        )
                    }

                    SyncAction.DELETE -> {
                        firebaseDataSource.delete(
                            unsyncedJetpack
                                .toFirebaseJetpack()
                                .copy(
                                    lastSynced = System.currentTimeMillis(),
                                ),
                        )
                    }

                    SyncAction.NONE -> {
                        // Do nothing
                    }
                }

                localDataSource.markAsSynced(unsyncedJetpack.id)
                emit(SyncProgress(total = totalSync, current = totalRemoteJetpacks + index + 1))
            }
        }
    }
}