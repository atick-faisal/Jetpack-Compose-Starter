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

package dev.atick.storage.preferences.data

import dev.atick.storage.preferences.models.DarkThemeConfig
import dev.atick.storage.preferences.models.Profile
import dev.atick.storage.preferences.models.ThemeBrand
import dev.atick.storage.preferences.models.UserData
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods to interact with user preferences data source.
 */
interface UserPreferencesDataSource {

    /**
     * A [Flow] that emits [UserData] representing user-specific data.
     */
    val userData: Flow<UserData>

    /**
     * Sets the user profile in the user preferences.
     *
     * @param profile The user ID to be set.
     */
    suspend fun setProfile(profile: Profile)

    /**
     * Sets the theme brand in the user preferences.
     *
     * @param themeBrand The theme brand to be set.
     */
    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfig The dark theme configuration to be set.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}
