package dev.atick.auth.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import dev.atick.storage.preferences.model.Profile

/**
 * Represents an authenticated user with basic information.
 *
 * @property id The unique identifier for the user.
 * @property name The user's name.
 * @property profilePictureUri The URI for the user's profile picture, or null if not available.
 */
data class AuthUser(
    val id: String,
    val name: String,
    val profilePictureUri: Uri?,
) {
    /**
     * Converts this [AuthUser] object to a [Profile] object.
     *
     * @return The corresponding [Profile] object.
     */
    fun asProfile(): Profile {
        return Profile(
            id = id,
            name = name,
            profilePictureUriString = profilePictureUri?.toString(),
        )
    }
}

/**
 * Converts a Firebase user object to an [AuthUser] object.
 * @return The corresponding [AuthUser] object.
 */
fun FirebaseUser.asAuthUser() = AuthUser(
    id = uid,
    name = displayName.orEmpty(),
    profilePictureUri = photoUrl,
)
