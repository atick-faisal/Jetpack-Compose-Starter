package dev.atick.storage.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: String = "-1",
    val themeBrand: ThemeBrand = ThemeBrand.DEFAULT,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
)
