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

package dev.atick.data.repository.settings

import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.utils.suspendRunCatching
import dev.atick.data.model.settings.DarkThemeConfig
import dev.atick.data.model.settings.Settings
import dev.atick.data.model.settings.asSettings
import dev.atick.data.model.settings.toDarkThemeConfigPreferences
import dev.atick.firebase.auth.data.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [SettingsRepository].
 *
 * @property authDataSource Data source for authentication.
 * @property userPreferencesDataSource Data source for user preferences.
 */
internal class SettingsRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : SettingsRepository {

    /**
     * Retrieves the user's settings.
     *
     * @return A Flow emitting the user's settings.
     */
    override fun getSettings(): Flow<Settings> =
        userPreferencesDataSource.getUserDataPreferences().map { it.asSettings() }

    /**
     * Sets the dark theme configuration.
     *
     * @param darkThemeConfig The dark theme configuration.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDarkThemeConfig(
                darkThemeConfig.toDarkThemeConfigPreferences(),
            )
        }
    }

    /**
     * Sets the dynamic color preference.
     *
     * @param useDynamicColor Whether to use dynamic colors.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        }
    }

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.resetUserPreferences()
        }
    }
}
