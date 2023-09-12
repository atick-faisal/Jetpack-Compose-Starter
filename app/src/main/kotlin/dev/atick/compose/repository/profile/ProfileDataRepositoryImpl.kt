package dev.atick.compose.repository.profile

import dev.atick.auth.data.AuthDataSource
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.storage.preferences.data.UserPreferencesDataSource
import dev.atick.storage.preferences.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ProfileDataRepository] that provides profile screen data and sign-out functionality.
 *
 * @param userPreferencesDataSource The data source for user preferences.
 * @param authDataSource The data source for authentication operations.
 */
class ProfileDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : ProfileDataRepository {

    /**
     * A flow that provides [ProfileScreenData] updates to be displayed on the profile screen.
     *
     * The profile screen data is derived from the user's preferences.
     */
    override val profileScreenData: Flow<ProfileScreenData> =
        userPreferencesDataSource.userData.map { userData ->
            ProfileScreenData(
                name = userData.name,
                profilePictureUri = userData.profilePictureUriString,
            )
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
        return runCatching {
            authDataSource.signOut()
            userPreferencesDataSource.setProfile(Profile())
        }
    }
}