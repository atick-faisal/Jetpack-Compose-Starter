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

import dev.atick.core.room.models.JetpackEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for managing local storage operations related to [JetpackEntity] objects.
 */
interface LocalDataSource {
    fun getJetpacks(): Flow<List<JetpackEntity>>
    fun getJetpack(id: String): Flow<JetpackEntity>
    suspend fun getUnsyncedJetpacks(): List<JetpackEntity>
    suspend fun insertJetpack(jetpackEntity: JetpackEntity)
    suspend fun upsertJetpack(jetpackEntity: JetpackEntity)
    suspend fun upsertJetpacks(remoteJetpacks: List<JetpackEntity>)
    suspend fun updateJetpack(jetpackEntity: JetpackEntity)
    suspend fun markJetpackAsDeleted(id: String)
    suspend fun deleteJetpackPermanently(id: String)
    suspend fun markAsSynced(id: String, timestamp: Long = System.currentTimeMillis())
    suspend fun getLatestUpdateTimestamp(): Long
}
