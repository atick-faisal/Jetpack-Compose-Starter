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

package dev.atick.data.model.settings

import dev.atick.core.preferences.model.DarkThemeConfigPreferences
import dev.atick.core.preferences.model.UserDataPreferences

/**
 * Data class representing editable user settings related to themes and appearance.
 *
 * @property useDynamicColor Indicates whether dynamic colors are enabled.
 * @property darkThemeConfig Configuration for the dark theme.
 * @property language The language of the app.
 * @constructor Creates a [Settings] instance with optional parameters.
 */
data class Settings(
    val userName: String? = null,
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val language: Language = Language.ENGLISH,
)

/**
 * Enum class representing configuration options for the dark theme.
 *
 * @property FOLLOW_SYSTEM The dark theme configuration follows the system-wide setting.
 * @property LIGHT The app's dark theme is disabled, using the light theme.
 * @property DARK The app's dark theme is enabled, using the dark theme.
 */
enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

/**
 * Enum class representing the supported languages.
 *
 * @property code The language code.
 */
enum class Language(val code: String) {
    ENGLISH("en"),
    ARABIC("ar"),
}

/**
 * Extension function to map [UserDataPreferences] to [Settings].
 *
 * @return The mapped [Settings].
 */
fun UserDataPreferences.asSettings(): Settings {
    return Settings(
        userName = userName,
        useDynamicColor = useDynamicColor,
        darkThemeConfig = darkThemeConfigPreferences.toDarkThemeConfig(),
    )
}

/**
 * Extension function to map [DarkThemeConfigPreferences] to [DarkThemeConfig].
 *
 * @return The mapped [DarkThemeConfig].
 */
fun DarkThemeConfigPreferences.toDarkThemeConfig(): DarkThemeConfig {
    return when (this) {
        DarkThemeConfigPreferences.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
        DarkThemeConfigPreferences.LIGHT -> DarkThemeConfig.LIGHT
        DarkThemeConfigPreferences.DARK -> DarkThemeConfig.DARK
    }
}

/**
 * Extension function to map [DarkThemeConfig] to [DarkThemeConfigPreferences].
 *
 * @return The mapped [DarkThemeConfigPreferences].
 */
fun DarkThemeConfig.toDarkThemeConfigPreferences(): DarkThemeConfigPreferences {
    return when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigPreferences.FOLLOW_SYSTEM
        DarkThemeConfig.LIGHT -> DarkThemeConfigPreferences.LIGHT
        DarkThemeConfig.DARK -> DarkThemeConfigPreferences.DARK
    }
}
