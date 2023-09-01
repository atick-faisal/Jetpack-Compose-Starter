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

package dev.atick.network.api

import dev.atick.network.model.NetworkPost
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Jetpack.
 */
interface JetpackRestApi {

    /**
     * Retrieves a list of network posts from the specified endpoint.
     *
     * This function uses the HTTP GET method to retrieve a list of network posts from the "/posts" endpoint.
     *
     * @return A [List] of [NetworkPost] objects representing the retrieved network posts.
     */
    @GET("/photos")
    suspend fun getPosts(): List<NetworkPost>

    /**
     * Retrieves a network post with the specified ID from the designated endpoint.
     *
     * This function uses the HTTP GET method to retrieve a single network post with the given ID from the "/posts/{id}" endpoint.
     *
     * @param id The ID of the network post to retrieve.
     * @return A [NetworkPost] object representing the retrieved network post.
     */
    @GET("/photos/{id}")
    suspend fun getPost(@Path("id") id: Int): NetworkPost
}
