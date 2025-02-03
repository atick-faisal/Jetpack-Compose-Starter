/*
 * Copyright 2023 Atick Faisal
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

package dev.atick.compose.repository.home

import dev.atick.compose.data.home.Jetpack
import dev.atick.compose.data.home.mapToJetpacks
import dev.atick.compose.data.home.toJetpack
import dev.atick.compose.data.home.toJetpackEntity
import dev.atick.core.network.data.NetworkDataSource
import dev.atick.core.utils.suspendRunCatching
import dev.atick.firebase.data.FirebaseDataSource
import dev.atick.firebase.models.FirebaseJetpack
import dev.atick.storage.preferences.data.UserPreferencesDataSource
import dev.atick.core.room.data.LocalDataSource
import dev.atick.core.room.models.JetpackEntity
import dev.atick.core.room.models.SyncAction
import dev.atick.sync.manager.SyncManager
import dev.atick.sync.utils.SyncProgress
import dev.atick.sync.utils.SyncableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [HomeRepository] that coordinates data synchronization between network and local sources.
 *
 * @param networkDataSource The data source for network operations.
 * @param localDataSource The data source for local storage operations.
 */
class HomeRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val preferencesDataSource: UserPreferencesDataSource,
    private val syncManager: SyncManager,
) : HomeRepository, SyncableRepository {
    override suspend fun sync(): Flow<SyncProgress> {
        return syncWithFirebase()
    }

    override fun getJetpacks(): Flow<List<Jetpack>> {
        return localDataSource.getJetpacks().map { it.mapToJetpacks() }
    }

    override fun getJetpack(id: String): Flow<Jetpack> {
        return localDataSource.getJetpack(id).map { it.toJetpack() }
    }

    override suspend fun insertJetpack(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.insertJetpack(
                jetpack.toJetpackEntity()
                    .copy(
                        lastUpdated = System.currentTimeMillis(),
                        needsSync = true,
                        syncAction = SyncAction.CREATE,
                    ),
            )
            syncManager.requestSync()
        }
    }

    override suspend fun updateJetpack(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.updateJetpack(
                jetpack.toJetpackEntity()
                    .copy(
                        lastUpdated = System.currentTimeMillis(),
                        needsSync = true,
                        syncAction = SyncAction.UPDATE,
                    ),
            )
            syncManager.requestSync()
        }
    }

    override suspend fun markJetpackAsDeleted(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.updateJetpack(
                jetpack.toJetpackEntity()
                    .copy(
                        lastUpdated = System.currentTimeMillis(),
                        needsSync = true,
                        syncAction = SyncAction.DELETE,
                    ),
            )
            syncManager.requestSync()
        }
    }

    private fun syncWithFirebase(): Flow<SyncProgress> {
        return flow {
            val userId = preferencesDataSource.userData.map { it.id }.first()

            val lastSynced = localDataSource.getLatestUpdateTimestamp()
            val remoteJetpacks = firebaseDataSource.pull(userId, lastSynced)
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
                        firebaseDataSource.create(userId, unsyncedJetpack.toFirebaseJetpack())
                    }

                    SyncAction.UPDATE -> {
                        firebaseDataSource.update(userId, unsyncedJetpack.toFirebaseJetpack())
                    }

                    SyncAction.DELETE -> {
                        firebaseDataSource.delete(userId, unsyncedJetpack.toFirebaseJetpack())
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

fun FirebaseJetpack.toJetpackEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        needsSync = false,
    )
}

fun JetpackEntity.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
    )
}
