package dev.atick.network.data

import dev.atick.network.api.JetpackRestApi
import dev.atick.network.data.models.GetItemResponse
import javax.inject.Inject

class JetpackDataSourceImpl @Inject constructor(
    private val jetpackRestApi: JetpackRestApi
): JetpackDataSource {
    override suspend fun getItem(id: Int): GetItemResponse {
        return jetpackRestApi.getItem(id)
    }
}