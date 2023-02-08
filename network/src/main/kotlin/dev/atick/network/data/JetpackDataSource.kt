package dev.atick.network.data

import dev.atick.network.data.models.GetItemResponse

interface JetpackDataSource {
    suspend fun getItem(id: Int): GetItemResponse
}