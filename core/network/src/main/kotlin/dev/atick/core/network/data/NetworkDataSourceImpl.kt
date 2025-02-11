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

package dev.atick.core.network.data

import dev.atick.core.di.IoDispatcher
import dev.atick.core.network.api.JetpackRestApi
import dev.atick.core.network.model.NetworkPost
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Data source implementation for Jetpack.
 *
 * @param jetpackRestApi The [JetpackRestApi] instance.
 */
internal class NetworkDataSourceImpl @Inject constructor(
    private val jetpackRestApi: JetpackRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : NetworkDataSource {

    /**
     * Retrieves a list of network posts from a remote source using the specified IO dispatcher.
     *
     * This function overrides the suspend function [getPosts] and fetches a list of network posts by invoking [jetpackRestApi.getPosts()]
     * within the provided IO dispatcher context.
     *
     * @return A [List] of [NetworkPost] objects representing the retrieved network posts.
     */
    override suspend fun getPosts(): List<NetworkPost> {
        return withContext(ioDispatcher) {
            jetpackRestApi.getPosts()
        }
    }

    /**
     * Retrieves a network post with the specified ID from the designated endpoint.
     *
     * This function uses the HTTP GET method to retrieve a single network post with the given ID from the "/posts/{id}" endpoint.
     *
     * @param id The ID of the network post to retrieve.
     * @return A [NetworkPost] object representing the retrieved network post.
     */
    override suspend fun getPost(id: Int): NetworkPost {
        return withContext(ioDispatcher) {
            jetpackRestApi.getPost(id)
        }
    }
}
