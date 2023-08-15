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
    val thumbnailUrl: String
)
