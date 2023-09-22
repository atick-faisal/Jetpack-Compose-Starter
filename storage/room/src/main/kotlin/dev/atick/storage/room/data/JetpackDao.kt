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

package dev.atick.storage.room.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.atick.storage.room.models.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for handling [PostEntity] operations.
 */
@Dao
interface JetpackDao {

    /**
     * Upsert operation (Insert an entity into the database. If the entity already exists, replace it.)
     * @param postEntity The entity to be inserted or updated.
     */
    @Upsert
    suspend fun insertOrUpdatePostEntity(postEntity: PostEntity)

    /**
     * Delete a [PostEntity] from the database.
     * @param postEntity The entity to be deleted.
     */
    @Delete
    suspend fun deletePostEntity(postEntity: PostEntity)

    /**
     * Retrieve a [PostEntity] by ID.
     * @param id The id of the entity.
     * @return The entity with the given id, or null if no such entity exists.
     */
    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getPostEntity(id: Int): PostEntity?

    /**
     * Retrieve all [PostEntity] from the database.
     * @return A [Flow] that emits the list of entities.
     */
    @Query("SELECT * FROM posts")
    fun getPostEntities(): Flow<List<PostEntity>>

    /**
     * Upsert operation (Insert a list of entities into the database. If an entity already exists, replace it.)
     * @param postEntities The list of entities to be inserted or updated.
     */
    @Upsert
    suspend fun upsertPostEntities(postEntities: List<PostEntity>)

    /**
     * Delete all [PostEntity] items from the database.
     */
    @Query("DELETE FROM posts")
    suspend fun deleteAllPostEntities()
}
