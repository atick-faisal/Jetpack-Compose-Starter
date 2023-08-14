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

package dev.atick.compose.repository.home

import dev.atick.compose.data.home.UiPost
import dev.atick.compose.data.home.mapToUiPosts
import dev.atick.compose.data.home.toUiPost
import dev.atick.network.NetworkDataSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) : HomeRepository {

    /**
     * Retrieves a list of UI posts wrapped in a [Result] by utilizing the network data source.
     *
     * This function asynchronously fetches a list of UI posts using the network data source and encapsulates the result
     * in a [Result] wrapper, applying the [mapToUiPosts] conversion.
     *
     * @return A [Result] instance containing either the fetched [List] of [UiPost] objects on success or an error on failure.
     */
    override suspend fun getPosts(): Result<List<UiPost>> {
        return kotlin.runCatching {
            networkDataSource.getPosts().mapToUiPosts()
        }
    }

    /**
     * Retrieves a UI post with the specified ID wrapped in a [Result] by utilizing the network data source.
     *
     * This function asynchronously fetches a UI post with the given ID using the network data source and encapsulates
     * the result in a [Result] wrapper, applying the [toUiPost] conversion.
     *
     * @param id The ID of the UI post to retrieve.
     * @return A [Result] instance containing either the fetched [UiPost] object on success or an error on failure.
     */
    override suspend fun getPost(id: Int): Result<UiPost> {
        return kotlin.runCatching {
            networkDataSource.getPost(id).toUiPost()
        }
    }

}
