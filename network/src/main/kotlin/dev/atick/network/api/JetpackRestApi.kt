package dev.atick.network.api

import dev.atick.network.data.models.GetItemResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JetpackRestApi {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    @GET("todos/{id}")
    suspend fun getItem(
        @Path("id") id: Int
    ): GetItemResponse
}