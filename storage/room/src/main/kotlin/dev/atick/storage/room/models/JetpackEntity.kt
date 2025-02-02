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

package dev.atick.storage.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

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

/**
 * Represents a JetpackEntity, which is a data structure for storing information about a jetpack.
 *
 * @property id The unique identifier for the jetpack entity.
 * @property name The name of the jetpack.
 * @property price The price of the jetpack.
 * @property lastSynced The timestamp of the last sync operation.
 * @property needsSync A flag indicating whether the jetpack needs to be synced.
 * @property deleted A flag indicating whether the jetpack has been deleted.
 * @property syncAction The action to take when syncing the jetpack.
 */
@Entity(tableName = "jetpacks")
data class JetpackEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val price: Double,

    // Sync metadata
    val lastUpdated: Long = 0,
    val lastSynced: Long = 0,
    val needsSync: Boolean = false,
    val deleted: Boolean = false,
    val syncAction: SyncAction = SyncAction.NONE,
)

/**
 * Represents a SyncAction, which is an enumeration of the possible actions to take when syncing
 * an item.
 */
enum class SyncAction {
    NONE,
    CREATE,
    UPDATE,
    DELETE,
}
