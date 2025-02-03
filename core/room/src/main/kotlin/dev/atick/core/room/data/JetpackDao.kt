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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import dev.atick.core.room.models.JetpackEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for handling [JetpackEntity] operations.
 */
@Dao
interface JetpackDao {
    @Query("SELECT * FROM jetpacks WHERE deleted = 0")
    fun getJetpacks(): Flow<List<JetpackEntity>>

    @Query("SELECT * FROM jetpacks WHERE id = :id")
    fun getJetpack(id: String): Flow<JetpackEntity>

    @Query("SELECT * FROM jetpacks WHERE lastUpdated > lastSynced OR needsSync = 1")
    suspend fun getUnsyncedJetpacks(): List<JetpackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJetpack(jetpackEntity: JetpackEntity)

    @Upsert
    suspend fun upsertJetpack(jetpackEntity: JetpackEntity)

    /**
     * Upserts (insert or update) jetpacks from a remote source.
     * If a jetpack already exists locally (matching ID), it will be updated with the remote version.
     * If it doesn't exist locally, a new entry will be created.
     *
     * @param remoteJetpacks List of jetpack entities from the remote source to be upserted
     * @return List of row IDs for the inserted rows
     */
    @Transaction
    @Upsert
    suspend fun upsertJetpacks(remoteJetpacks: List<JetpackEntity>)

    @Update
    suspend fun updateJetpack(jetpackEntity: JetpackEntity)

    @Query("UPDATE jetpacks SET deleted = 1, needsSync = 1, syncAction = 'DELETE' WHERE id = :id")
    suspend fun markJetpackAsDeleted(id: String)

    @Query("DELETE FROM jetpacks WHERE id = :id")
    suspend fun deleteJetpackPermanently(id: String)

    @Query("UPDATE jetpacks SET needsSync = 0, syncAction = 'NONE', lastSynced = :timestamp WHERE id = :id")
    suspend fun markAsSynced(id: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Gets the most recent lastUpdated timestamp from all jetpacks in the database.
     * This can be used as a reference point for fetching only newer items from remote.
     *
     * @return The most recent lastUpdated timestamp, or 0 if no jetpacks exist
     */
    @Query("SELECT MAX(lastUpdated) FROM jetpacks WHERE deleted = 0")
    suspend fun getLatestUpdateTimestamp(): Long?
}
