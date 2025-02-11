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
 * Represents a user profile.
 *
 * This data class is used for storing information about a user's profile.
 *
 * @property id The unique identifier for the profile. Defaults to empty if not provided.
 * @property userName The name of the user. Defaults to empty if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 */
@Serializable
data class PreferencesUserProfile(
    val id: String = String(),
    val userName: String = String(),
    val profilePictureUriString: String? = null,
)
