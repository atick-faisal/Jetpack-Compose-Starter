package dev.atick.auth.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import dev.atick.storage.preferences.model.Profile

data class AuthUser(
    val id: String,
    val name: String,
    val profilePictureUri: Uri?,
) {
    fun asProfile(): Profile {
        return Profile(
            id = id,
            name = name,
            profilePictureUriString = profilePictureUri?.toString(),
        )
    }
}

fun FirebaseUser.asAuthUser() = AuthUser(
    id = uid,
    name = displayName ?: "No Name",
    profilePictureUri = photoUrl,
)