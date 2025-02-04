package dev.atick.data.models

import dev.atick.core.preferences.models.PreferencesUserProfile
import dev.atick.core.preferences.models.UserDataPreferences

data class Profile(
    val userName: String,
    val profilePictureUri: String? = null,
)

fun UserDataPreferences.toProfile(): Profile {
    return Profile(
        userName = userName ?: "Anonymous",
        profilePictureUri = profilePictureUriString,
    )
}

fun Profile.toPreferencesUserProfile(): PreferencesUserProfile {
    return PreferencesUserProfile(
        userName = userName,
        profilePictureUriString = profilePictureUri,
    )
}