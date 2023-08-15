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

package dev.atick.compose.repository.user

import dev.atick.storage.preferences.UserPreferencesDataSource
import dev.atick.storage.preferences.model.DarkThemeConfig
import dev.atick.storage.preferences.model.ThemeBrand
import dev.atick.storage.preferences.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [UserDataRepository] that utilizes [UserPreferencesDataSource] to manage user data and preferences.
 *
 * @property userPreferencesDataSource The data source for user preferences.
 */
class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    /**
     * A [Flow] that emits [UserData] representing user-specific data.
     */
    override val userData: Flow<UserData>
        get() = userPreferencesDataSource.userData

    /**
     * Sets the user ID in the user preferences.
     *
     * @param userId The user ID to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setUserId(userId: String): Result<Unit> {
        return runCatching {
            userPreferencesDataSource.setUserId(userId)
        }
    }

    /**
     * Sets the theme brand in the user preferences.
     *
     * @param themeBrand The theme brand to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setThemeBrand(themeBrand: ThemeBrand): Result<Unit> {
        return runCatching {
            userPreferencesDataSource.setThemeBrand(themeBrand)
        }
    }

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfig The dark theme configuration to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return runCatching {
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
        return runCatching {
            userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        }
    }
}
