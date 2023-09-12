package dev.atick.compose.repository.profile

import dev.atick.auth.data.AuthDataSource
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.storage.preferences.UserPreferencesDataSource
import dev.atick.storage.preferences.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : ProfileDataRepository {

    override val profileScreenData: Flow<ProfileScreenData> =
        userPreferencesDataSource.userData.map { userData ->
            ProfileScreenData(
                name = userData.name,
                profilePictureUri = userData.profilePictureUriString,
            )
        }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            authDataSource.signOut()
            userPreferencesDataSource.setProfile(Profile())
        }
    }
}