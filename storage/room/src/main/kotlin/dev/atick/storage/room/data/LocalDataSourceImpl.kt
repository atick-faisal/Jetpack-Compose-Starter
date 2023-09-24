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

import dev.atick.core.di.IoDispatcher
import dev.atick.storage.room.models.PostEntity
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

    /**
     * Inserts or updates a [PostEntity] object in the local storage.
     *
     * @param postEntity The [PostEntity] object to be inserted or updated.
     */
    override suspend fun insertOrUpdatePostEntity(postEntity: PostEntity) {
        withContext(ioDispatcher) {
            jetpackDao.insertOrUpdatePostEntity(postEntity)
        }
    }

    /**
     * Deletes a [PostEntity] object from the local storage.
     *
     * @param postEntity The [PostEntity] object to be deleted.
     */
    override suspend fun deletePostEntity(postEntity: PostEntity) {
        withContext(ioDispatcher) {
            jetpackDao.deletePostEntity(postEntity)
        }
    }

    /**
     * Retrieves a [PostEntity] object from the local storage based on its unique identifier.
     *
     * @param id The unique identifier of the [PostEntity] to retrieve.
     * @return The retrieved [PostEntity] object, or null if not found.
     */
    override suspend fun getPostEntity(id: Int): PostEntity? {
        return withContext(ioDispatcher) {
            jetpackDao.getPostEntity(id)
        }
    }

    /**
     * Retrieves a [Flow] of [List] of [PostEntity] objects from the local storage.
     *
     * @return A [Flow] emitting a list of [PostEntity] objects.
     */
    override fun getPostEntities(): Flow<List<PostEntity>> {
        return jetpackDao.getPostEntities().flowOn(ioDispatcher)
    }

    /**
     * Inserts or updates a list of [PostEntity] objects in the local storage.
     *
     * @param postEntities The list of [PostEntity] objects to be inserted or updated.
     */
    override suspend fun upsertPostEntities(postEntities: List<PostEntity>) {
        withContext(ioDispatcher) {
            jetpackDao.upsertPostEntities(postEntities)
        }
    }

    /**
     * Deletes all [PostEntity] objects from the local storage.
     */
    override suspend fun deleteAllPostEntities() {
        withContext(ioDispatcher) {
            jetpackDao.deleteAllPostEntities()
        }
    }
}
