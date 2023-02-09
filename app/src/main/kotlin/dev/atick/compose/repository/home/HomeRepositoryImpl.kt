package dev.atick.compose.repository.home

import dev.atick.compose.data.home.Item
import dev.atick.network.data.JetpackDataSource
import dev.atick.storage.preferences.data.PreferencesDatastore
import dev.atick.storage.room.data.JetpackDao
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val jetpackDao: JetpackDao,
    private val jetpackDataSource: JetpackDataSource,
    private val preferencesDatastore: PreferencesDatastore
) : HomeRepository {
    override suspend fun getItem(id: Int): Result<Item> {
        return try {
            val response = jetpackDataSource.getItem(id)
            val item = Item(
                id = response.id,
                title = response.title
            )
            Result.success(item)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun saveItem(item: Item) {
        jetpackDao.insert(item.toRoomItem())
    }

    override suspend fun getUserId(): Result<String> {
        return try {
            val userId = preferencesDatastore.getUserId()
            Result.success(userId)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}