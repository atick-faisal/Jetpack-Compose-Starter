package dev.atick.compose.repository.home

import dev.atick.compose.data.home.Item
import dev.atick.network.data.JetpackDataSource
import javax.inject.Inject

class JetpackRepositoryImpl @Inject constructor(
    private val jetpackDataSource: JetpackDataSource
) : JetpackRepository {
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
}