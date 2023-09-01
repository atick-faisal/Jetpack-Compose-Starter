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
import dev.atick.compose.data.home.mapToPostEntities
import dev.atick.compose.data.home.mapToUiPost
import dev.atick.compose.data.home.toUiPost
import dev.atick.network.NetworkDataSource
import dev.atick.storage.room.LocalDataSource
import dev.atick.storage.room.model.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [PostsRepository] that coordinates data synchronization between network and local sources.
 *
 * @param networkDataSource The data source for network operations.
 * @param localDataSource The data source for local storage operations.
 */
class PostsRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
) : PostsRepository {

    /**
     * Synchronizes posts by fetching from the network and updating the local storage.
     *
     * @return A [Result] indicating the outcome of the synchronization operation.
     */
    override suspend fun synchronizePosts(): Result<Unit> {
        return runCatching {
            val networkPosts = networkDataSource.getPosts()
            localDataSource.upsertPostEntities(networkPosts.mapToPostEntities())
        }
    }

    /**
     * Retrieves a post by its unique identifier from the network data source and converts it to a [UiPost].
     *
     * @param id The unique identifier of the post.
     * @return A [Result] containing the retrieved [UiPost] object.
     */
    override suspend fun getPost(id: Int): Result<UiPost> {
        return runCatching {
            networkDataSource.getPost(id).toUiPost()
        }
    }

    /**
     * Retrieves cached posts from the local data source and converts them to a [Flow] of [UiPost] objects.
     *
     * @return A [Flow] emitting a list of [UiPost] objects representing cached posts.
     */
    override fun getCachedPosts(): Flow<List<UiPost>> {
        return localDataSource.getPostEntities().map(List<PostEntity>::mapToUiPost)
    }
}
