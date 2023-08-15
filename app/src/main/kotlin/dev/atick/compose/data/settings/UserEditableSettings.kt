package dev.atick.compose.data.settings

import dev.atick.storage.preferences.model.DarkThemeConfig
import dev.atick.storage.preferences.model.ThemeBrand

data class UserEditableSettings(
    val brand: ThemeBrand = ThemeBrand.DEFAULT,
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
)
