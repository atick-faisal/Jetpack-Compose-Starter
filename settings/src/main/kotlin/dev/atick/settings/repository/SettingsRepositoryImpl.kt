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

package dev.atick.settings.repository

import dev.atick.auth.data.AuthDataSource
import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.preferences.models.DarkThemeConfig
import dev.atick.core.preferences.models.Profile
import dev.atick.core.preferences.models.UserData
import dev.atick.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [dev.atick.settings.repository.SettingsRepository] that utilizes [UserPreferencesDataSource] to manage user data and preferences.
 *
 * @property userPreferencesDataSource The data source for user preferences.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : SettingsRepository {

    /**
     * A [Flow] that emits [UserData] representing user-specific data.
     */
    override val userData: Flow<UserData>
        get() = userPreferencesDataSource.userData

    /**
     * Sets the user [Profile] in the user preferences.
     *
     * @param profile The user [Profile] to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setUserProfile(profile: Profile): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setProfile(profile)
        }
    }

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfig The dark theme configuration to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        }
    }

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        }
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
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.setProfile(Profile())
        }
    }
}
