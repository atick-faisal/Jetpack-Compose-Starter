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

package dev.atick.core.preferences.model

import kotlinx.serialization.Serializable

/**
 * Represents user data saved in Shared Preferences.
 *
 * This data class is used to store information about a user, including their ID, name, profile picture URI string,
 * preferred theme brand, dark theme configuration, and dynamic color preference.
 *
 * @property id The unique identifier for the user. Defaults to empty if not provided.
 * @property userName The name of the user. Defaults to "No Name" if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 * @property darkThemeConfigPreferences The user's preferred dark theme configuration. Defaults to [DarkThemeConfigPreferences.FOLLOW_SYSTEM].
 * @property useDynamicColor A boolean indicating whether the user prefers dynamic colors. Defaults to `true`.
 */
@Serializable
data class UserDataPreferences(
    val id: String = String(),
    val userName: String? = null,
    val profilePictureUriString: String? = null,
    val darkThemeConfigPreferences: DarkThemeConfigPreferences = DarkThemeConfigPreferences.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
)
