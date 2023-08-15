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
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for interacting with the home repository.
 */
interface PostsRepository {

    suspend fun synchronizePosts(): Result<Unit>

    /**
     * Retrieves a UI post with the specified ID wrapped in a [Result] from a data source.
     *
     * This function asynchronously fetches a UI post with the given ID and encapsulates the result in a [Result] wrapper.
     *
     * @param id The ID of the UI post to retrieve.
     * @return A [Result] instance containing either the fetched [UiPost] object on success or an error on failure.
     */
    suspend fun getPost(id: Int): Result<UiPost>

    fun getCachedPosts(): Flow<List<UiPost>>
}
