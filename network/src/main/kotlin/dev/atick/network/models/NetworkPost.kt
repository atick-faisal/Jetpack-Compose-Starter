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

package dev.atick.network.models

/**
 * Data class representing a network post retrieved from a remote source.
 *
 * @property id The unique identifier of the network post.
 * @property title The title of the network post.
 * @property url The URL associated with the network post.
 * @property thumbnailUrl The URL of the thumbnail image associated with the network post.
 */
data class NetworkPost(
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)
