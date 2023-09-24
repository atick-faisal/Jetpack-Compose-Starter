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

package dev.atick.compose.repository.profile

import dev.atick.auth.data.AuthDataSource
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.storage.preferences.data.UserPreferencesDataSource
import dev.atick.storage.preferences.models.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ProfileDataRepository] that provides profile screen data and sign-out functionality.
 *
 * @param userPreferencesDataSource The data source for user preferences.
 * @param authDataSource The data source for authentication operations.
 */
class ProfileDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : ProfileDataRepository {

    /**
     * A flow that provides [ProfileScreenData] updates to be displayed on the profile screen.
     *
     * The profile screen data is derived from the user's preferences.
     */
    override val profileScreenData: Flow<ProfileScreenData> =
        userPreferencesDataSource.userData.map { userData ->
            ProfileScreenData(
                name = userData.name,
                profilePictureUri = userData.profilePictureUriString,
            )
        }

    /**
     * Suspend function to sign the user out.
     *
     * This function signs the user out by delegating to the [AuthDataSource] and then updates the user's
     * profile data in [UserPreferencesDataSource].
     *
     * @return A [Result] representing the sign-out operation result. It contains [Unit] if
     * the sign-out was successful, or an error if there was a problem.
     */
    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            authDataSource.signOut()
            userPreferencesDataSource.setProfile(Profile())
        }
    }
}
