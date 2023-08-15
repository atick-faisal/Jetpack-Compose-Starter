package dev.atick.compose.repository.user

import dev.atick.storage.preferences.model.DarkThemeConfig
import dev.atick.storage.preferences.model.ThemeBrand
import dev.atick.storage.preferences.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods to interact with user data and preferences.
 */
interface UserDataRepository {

    /**
     * A [Flow] that emits [UserData] representing user-specific data.
     */
    val userData: Flow<UserData>

    /**
     * Sets the user ID in the user preferences.
     *
     * @param userId The user ID to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    suspend fun setUserId(userId: String): Result<Unit>

    /**
     * Sets the theme brand in the user preferences.
     *
     * @param themeBrand The theme brand to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    suspend fun setThemeBrand(themeBrand: ThemeBrand): Result<Unit>

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfig The dark theme configuration to be set.
     * @return [Result] indicating the success or failure of the operation.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit>

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     * @return [Result] indicating the success or failure of the operation.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit>
}
