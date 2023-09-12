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

import dev.atick.storage.room.model.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for managing local storage operations related to [PostEntity] objects.
 */
interface LocalDataSource {

    /**
     * Inserts or updates a [PostEntity] object in the local storage.
     *
     * @param postEntity The [PostEntity] object to be inserted or updated.
     */
    suspend fun insertOrUpdatePostEntity(postEntity: PostEntity)

    /**
     * Deletes a [PostEntity] object from the local storage.
     *
     * @param postEntity The [PostEntity] object to be deleted.
     */
    suspend fun deletePostEntity(postEntity: PostEntity)

    /**
     * Retrieves a [PostEntity] object from the local storage based on its unique identifier.
     *
     * @param id The unique identifier of the [PostEntity] to retrieve.
     * @return The retrieved [PostEntity] object, or null if not found.
     */
    suspend fun getPostEntity(id: Int): PostEntity?

    /**
     * Retrieves a [Flow] of [List] of [PostEntity] objects from the local storage.
     *
     * @return A [Flow] emitting a list of [PostEntity] objects.
     */
    fun getPostEntities(): Flow<List<PostEntity>>

    /**
     * Inserts or updates a list of [PostEntity] objects in the local storage.
     *
     * @param postEntities The list of [PostEntity] objects to be inserted or updated.
     */
    suspend fun upsertPostEntities(postEntities: List<PostEntity>)

    /**
     * Deletes all [PostEntity] objects from the local storage.
     */
    suspend fun deleteAllPostEntities()
}
