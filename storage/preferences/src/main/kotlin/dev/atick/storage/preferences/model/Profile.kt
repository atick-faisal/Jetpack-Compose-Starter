package dev.atick.storage.preferences.model

import kotlinx.serialization.Serializable

/**
 * Represents a user profile.
 *
 * This data class is used for storing information about a user's profile.
 *
 * @property id The unique identifier for the profile. Defaults to "-1" if not provided.
 * @property name The name of the user. Defaults to "No Name" if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 */
@Serializable
data class Profile(
    val id: String = "-1",
    val name: String = "No Name",
    val profilePictureUriString: String? = null,
)
