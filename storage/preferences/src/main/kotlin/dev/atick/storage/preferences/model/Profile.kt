package dev.atick.storage.preferences.model

import kotlinx.serialization.Serializable

/**
 * Represents a user profile.
 *
 * This data class is used for storing information about a user's profile.
 *
 * @property id The unique identifier for the profile. Defaults to empty if not provided.
 * @property name The name of the user. Defaults to empty if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 */
@Serializable
data class Profile(
    val id: String = String(),
    val name: String = String(),
    val profilePictureUriString: String? = null,
)
