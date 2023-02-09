package dev.atick.storage.room.data

import androidx.room.*
import dev.atick.storage.room.data.models.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface JetpackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Long): Item?

    @Query("SELECT * FROM items WHERE name = :name LIMIT 1")
    suspend fun getItem(name: String): Item?

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Query("DELETE FROM items")
    suspend fun clear()
}