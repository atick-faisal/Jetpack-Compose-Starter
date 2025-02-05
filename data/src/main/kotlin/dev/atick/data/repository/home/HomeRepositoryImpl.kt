package dev.atick.data.repository.home

import dev.atick.core.room.data.LocalDataSource
import dev.atick.core.room.models.SyncAction
import dev.atick.core.utils.suspendRunCatching
import dev.atick.data.models.home.Jetpack
import dev.atick.data.models.home.mapToJetpacks
import dev.atick.data.models.home.toFirebaseJetpack
import dev.atick.data.models.home.toJetpack
import dev.atick.data.models.home.toJetpackEntity
import dev.atick.data.utils.SyncProgress
import dev.atick.firebase.firestore.data.FirebaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val firebaseDataSource: FirebaseDataSource,
) : HomeRepository {
    override fun getJetpacks(): Flow<List<Jetpack>> {
        return localDataSource.getJetpacks().map { it.mapToJetpacks() }
    }

    override fun getJetpack(id: String): Flow<Jetpack> {
        return localDataSource.getJetpack(id).map { it.toJetpack() }
    }

    override suspend fun createOrUpdateJetpack(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.updateJetpack(jetpack.toJetpackEntity())
            firebaseDataSource.createOrUpdate(jetpack.toFirebaseJetpack())
            localDataSource.markAsSynced(jetpack.id)
        }
    }

    override suspend fun markJetpackAsDeleted(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.markJetpackAsDeleted(jetpack.id)
            firebaseDataSource.delete(jetpack.toFirebaseJetpack())
            localDataSource.markAsSynced(jetpack.id)
        }
    }

    override suspend fun sync(): Flow<SyncProgress> {
        return flow {
            val lastSynced = localDataSource.getLatestUpdateTimestamp()
            val remoteJetpacks = firebaseDataSource.pull(lastSynced)
            val totalRemoteJetpacks = remoteJetpacks.size

            val unsyncedJetpacks = localDataSource.getUnsyncedJetpacks()
            val totalUnsyncedJetpacks = unsyncedJetpacks.size

            val totalSync = totalRemoteJetpacks + totalUnsyncedJetpacks

            // Pull updates from remote
            remoteJetpacks.forEachIndexed { index, remoteJetpack ->
                localDataSource.upsertJetpack(remoteJetpack.toJetpackEntity())
                emit(SyncProgress(total = totalSync, current = index + 1))
            }

            // Push updates to remote
            unsyncedJetpacks.forEachIndexed { index, unsyncedJetpack ->
                when (unsyncedJetpack.syncAction) {
                    SyncAction.CREATE -> {
                        firebaseDataSource.create(unsyncedJetpack.toFirebaseJetpack())
                    }

                    SyncAction.UPDATE -> {
                        firebaseDataSource.createOrUpdate(unsyncedJetpack.toFirebaseJetpack())
                    }

                    SyncAction.DELETE -> {
                        firebaseDataSource.delete(unsyncedJetpack.toFirebaseJetpack())
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