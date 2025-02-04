package dev.atick.data.repository.profile

import dev.atick.data.models.Profile
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing profile-related operations.
 */
interface ProfileRepository {
    /**
     * Retrieves the profile information.
     *
     * @return A Flow emitting the Profile object.
     */
    fun getProfile(): Flow<Profile>

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun signOut(): Result<Unit>
}