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
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.atick.storage.room.data.models.Item
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for [Item] entity.
 */
@Dao
interface JetpackDao {
    /**
     * Insert an item into the database. If the item already exists, replace it.
     *
     * @param item The item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    /**
     * Update an item.
     *
     * @param item The item to be updated.
     */
    @Update
    suspend fun update(item: Item)

    /**
     * Delete an item.
     *
     * @param item The item to be deleted.
     */
    @Delete
    suspend fun delete(item: Item)

    /**
     * Delete all items.
     */
    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Long): Item?

    /**
     * Get an item by name.
     *
     * @param name The name of the item.
     * @return The item with the given name.
     */
    @Query("SELECT * FROM items WHERE name = :name LIMIT 1")
    suspend fun getItem(name: String): Item?

    /**
     * Get all items.
     *
     * @return A list of all items in the database.
     */
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    /**
     * Insert a list of items into the database. If the item already exists, replace it.
     *
     * @param items The list of items to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    /**
     * Delete all items.
     */
    @Query("DELETE FROM items")
    suspend fun clear()
}
