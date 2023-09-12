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
