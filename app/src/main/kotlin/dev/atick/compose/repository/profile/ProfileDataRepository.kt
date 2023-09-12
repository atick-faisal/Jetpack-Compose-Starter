package dev.atick.compose.repository.profile

import dev.atick.compose.data.profile.ProfileScreenData
import kotlinx.coroutines.flow.Flow

interface ProfileDataRepository {
    val profileScreenData: Flow<ProfileScreenData>
    suspend fun signOut(): Result<Unit>
}