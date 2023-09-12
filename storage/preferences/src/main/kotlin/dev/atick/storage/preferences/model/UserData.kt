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

package dev.atick.storage.preferences.model

import kotlinx.serialization.Serializable

/**
 * Represents user data.
 *
 * This data class is used to store information about a user, including their ID, name, profile picture URI string,
 * preferred theme brand, dark theme configuration, and dynamic color preference.
 *
 * @property id The unique identifier for the user. Defaults to empty if not provided.
 * @property name The name of the user. Defaults to "No Name" if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 * @property themeBrand The preferred theme brand for the user. Defaults to [ThemeBrand.DEFAULT].
 * @property darkThemeConfig The user's preferred dark theme configuration. Defaults to [DarkThemeConfig.FOLLOW_SYSTEM].
 * @property useDynamicColor A boolean indicating whether the user prefers dynamic colors. Defaults to `true`.
 */
@Serializable
data class UserData(
    val id: String = String(),
    val name: String = "No Name",
    val profilePictureUriString: String? = null,
    val themeBrand: ThemeBrand = ThemeBrand.DEFAULT,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
)
