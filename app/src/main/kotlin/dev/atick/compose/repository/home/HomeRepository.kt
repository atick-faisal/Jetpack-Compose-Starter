package dev.atick.compose.repository.home

import dev.atick.compose.data.home.Item

interface HomeRepository {
    suspend fun getItem(id: Int): Result<Item>
    suspend fun saveItem(item: Item)
    suspend fun getUserId(): Result<String>
}