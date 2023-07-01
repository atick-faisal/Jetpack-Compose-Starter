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

import dev.atick.compose.data.home.Item
import dev.atick.network.data.JetpackDataSource
import dev.atick.storage.preferences.data.PreferencesDatastore
import dev.atick.storage.room.data.JetpackDao
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val jetpackDao: JetpackDao,
    private val jetpackDataSource: JetpackDataSource,
    private val preferencesDatastore: PreferencesDatastore,
) : HomeRepository {

    /**
     * Retrieves an item with the specified ID.
     *
     * @param id The ID of the item to retrieve.
     * @return A [Result] object representing the result of the operation.
     */
    override suspend fun getItem(id: Int): Result<Item> {
        return try {
            val response = jetpackDataSource.getItem(id)
            val item = Item(
                id = response.id,
                title = response.title,
            )
            Result.success(item)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    /**
     * Saves an item to the repository.
     *
     * @param item The item to save.
     */
    override suspend fun saveItem(item: Item) {
        jetpackDao.insert(item.toRoomItem())
    }

    /**
     * Retrieves the user ID.
     *
     * @return A [Result] object representing the result of the operation.
     */
    override suspend fun getUserId(): Result<String> {
        return try {
            val userId = preferencesDatastore.getUserId()
            Result.success(userId)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
