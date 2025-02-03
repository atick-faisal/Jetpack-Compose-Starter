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

package dev.atick.core.room.data

import dev.atick.core.di.IoDispatcher
import dev.atick.core.room.data.LocalDataSource
import dev.atick.core.room.models.JetpackEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [LocalDataSource] that interacts with the local storage using [JetpackDao].
 *
 * @param jetpackDao The data access object for performing database operations.
 * @param ioDispatcher The coroutine dispatcher for performing IO-bound tasks.
 */
class LocalDataSourceImpl @Inject constructor(
    private val jetpackDao: JetpackDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocalDataSource {

    override fun getJetpacks(): Flow<List<JetpackEntity>> =
        jetpackDao.getJetpacks().flowOn(ioDispatcher)

    override fun getJetpack(id: String): Flow<JetpackEntity> =
        jetpackDao.getJetpack(id).flowOn(ioDispatcher)

    override suspend fun getUnsyncedJetpacks(): List<JetpackEntity> = withContext(ioDispatcher) {
        jetpackDao.getUnsyncedJetpacks()
    }

    override suspend fun insertJetpack(jetpackEntity: JetpackEntity) = withContext(ioDispatcher) {
        jetpackDao.insertJetpack(jetpackEntity)
    }

    override suspend fun upsertJetpack(jetpackEntity: JetpackEntity) = withContext(ioDispatcher) {
        jetpackDao.upsertJetpack(jetpackEntity)
    }

    override suspend fun upsertJetpacks(remoteJetpacks: List<JetpackEntity>) =
        withContext(ioDispatcher) {
            jetpackDao.upsertJetpacks(remoteJetpacks)
        }

    override suspend fun updateJetpack(jetpackEntity: JetpackEntity) = withContext(ioDispatcher) {
        jetpackDao.updateJetpack(jetpackEntity)
    }

    override suspend fun markJetpackAsDeleted(id: String) = withContext(ioDispatcher) {
        jetpackDao.markJetpackAsDeleted(id)
    }

    override suspend fun deleteJetpackPermanently(id: String) = withContext(ioDispatcher) {
        jetpackDao.deleteJetpackPermanently(id)
    }

    override suspend fun markAsSynced(id: String, timestamp: Long) = withContext(ioDispatcher) {
        jetpackDao.markAsSynced(id, timestamp)
    }

    override suspend fun getLatestUpdateTimestamp(): Long = withContext(ioDispatcher) {
        jetpackDao.getLatestUpdateTimestamp() ?: 0
    }
}
