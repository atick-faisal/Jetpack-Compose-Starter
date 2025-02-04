package dev.atick.data.models.settings

import dev.atick.core.preferences.models.DarkThemeConfigPreferences
import dev.atick.core.preferences.models.UserDataPreferences

/**
 * Data class representing editable user settings related to themes and appearance.
 *
 * @property useDynamicColor Indicates whether dynamic colors are enabled.
 * @property darkThemeConfig Configuration for the dark theme.
 * @constructor Creates a [Settings] instance with optional parameters.
 */
data class Settings(
    val userName: String? = null,
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
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

fun UserDataPreferences.asSettings(): Settings {
    return Settings(
        userName = userName,
        useDynamicColor = useDynamicColor,
        darkThemeConfig = darkThemeConfigPreferences.asDarkThemeConfig(),
    )
}

fun DarkThemeConfigPreferences.asDarkThemeConfig(): DarkThemeConfig {
    return when (this) {
        DarkThemeConfigPreferences.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
        DarkThemeConfigPreferences.LIGHT -> DarkThemeConfig.LIGHT
        DarkThemeConfigPreferences.DARK -> DarkThemeConfig.DARK
    }
}

fun DarkThemeConfig.asDarkThemeConfigPreferences(): DarkThemeConfigPreferences {
    return when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigPreferences.FOLLOW_SYSTEM
        DarkThemeConfig.LIGHT -> DarkThemeConfigPreferences.LIGHT
        DarkThemeConfig.DARK -> DarkThemeConfigPreferences.DARK
    }
}