package dev.atick.data.repository.settings

import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.preferences.models.Profile
import dev.atick.core.utils.suspendRunCatching
import dev.atick.data.models.settings.DarkThemeConfig
import dev.atick.data.models.settings.Settings
import dev.atick.data.models.settings.asDarkThemeConfigPreferences
import dev.atick.data.models.settings.asSettings
import dev.atick.firebase.auth.data.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : SettingsRepository {
    override val settings: Flow<Settings>
        get() = userPreferencesDataSource.userDataPreferences.map { it.asSettings() }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDarkThemeConfig(
                darkThemeConfig.asDarkThemeConfigPreferences(),
            )
        }
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.setProfile(Profile())
        }
    }
}