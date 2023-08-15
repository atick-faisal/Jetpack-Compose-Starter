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

package dev.atick.storage.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a PostEntity, which is a data structure for storing information about a post.
 *
 * @property id The unique identifier for the post entity.
 * @property title The title of the post.
 * @property url The URL associated with the post.
 * @property thumbnailUrl The URL of the thumbnail image associated with the post.
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)
