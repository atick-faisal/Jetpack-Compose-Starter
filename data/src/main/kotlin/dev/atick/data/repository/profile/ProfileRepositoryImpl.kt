package dev.atick.data.repository.profile

import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.utils.suspendRunCatching
import dev.atick.data.models.Profile
import dev.atick.data.models.toProfile
import dev.atick.firebase.auth.data.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : ProfileRepository {
    override fun getProfile(): Flow<Profile> {
        return userPreferencesDataSource.userDataPreferences.map { it.toProfile() }
    }

    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.resetUserPreferences()
        }
    }
}