package dev.atick.compose.repository.home

import dev.atick.compose.data.home.Item

interface JetpackRepository {
    suspend fun getItem(id: Int): Result<Item>
}