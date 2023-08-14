package dev.atick.network.model

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