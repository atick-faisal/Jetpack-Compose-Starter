/*
 * Copyright 2025 Atick Faisal
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

package dev.atick.data.model.profile

import dev.atick.core.preferences.model.PreferencesUserProfile
import dev.atick.core.preferences.model.UserDataPreferences

/**
 * Data class representing a user profile.
 *
 * @property userName The name of the user.
 * @property profilePictureUri The URI of the user's profile picture.
 */
data class Profile(
    val userName: String = String(),
    val profilePictureUri: String? = null,
)

/**
 * Extension function to convert UserDataPreferences to Profile.
 *
 * @return A Profile object with data from UserDataPreferences.
 */
fun UserDataPreferences.toProfile(): Profile {
    return Profile(
        userName = userName ?: "Anonymous",
        profilePictureUri = profilePictureUriString,
    )
}

/**
 * Extension function to convert Profile to PreferencesUserProfile.
 *
 * @return A PreferencesUserProfile object with data from Profile.
 */
fun Profile.toPreferencesUserProfile(): PreferencesUserProfile {
    return PreferencesUserProfile(
        userName = userName,
        profilePictureUriString = profilePictureUri,
    )
}
