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

package dev.atick.compose.data.home

import dev.atick.network.model.NetworkPost

data class HomeScreenData(
    val posts: List<UiPost> = listOf(),
)

/**
 * Data class representing a post to be shown on the ui.
 *
 * @property id The unique identifier of the network post.
 * @property title The title of the network post.
 * @property url The URL associated with the network post.
 * @property thumbnailUrl The URL of the thumbnail image associated with the network post.
 */
data class UiPost(
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)

/**
 * Converts a [NetworkPost] object to a corresponding [UiPost] object.
 *
 * This extension function facilitates the conversion of a [NetworkPost] object into a [UiPost] object by mapping
 * the properties from the source to the destination.
 *
 * @receiver The [NetworkPost] object to convert to [UiPost].
 * @return A new [UiPost] object with properties mapped from the source [NetworkPost].
 */
fun NetworkPost.toUiPost(): UiPost {
    return UiPost(
        id = id,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl
    )
}

/**
 * Converts a list of [NetworkPost] objects to a list of corresponding [UiPost] objects.
 *
 * This extension function applies the [NetworkPost.toUiPost] conversion function to each element of the source list,
 * effectively mapping the properties from [NetworkPost] objects to [UiPost] objects.
 *
 * @receiver The list of [NetworkPost] objects to convert to a list of [UiPost] objects.
 * @return A new list containing [UiPost] objects with properties mapped from the source [NetworkPost] objects.
 */
fun List<NetworkPost>.mapToUiPosts(): List<UiPost> {
    return map(NetworkPost::toUiPost)
}

