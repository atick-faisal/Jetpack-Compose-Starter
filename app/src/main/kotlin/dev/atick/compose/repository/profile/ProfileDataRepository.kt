package dev.atick.compose.repository.profile

import dev.atick.compose.data.profile.ProfileScreenData
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing a repository for profile screen data and sign-out functionality.
 */
interface ProfileDataRepository {
    /**
     * A flow that provides [ProfileScreenData] updates to be displayed on the profile screen.
     *
     * The profile screen data can change over time, and clients can collect updates using a Flow.
     */
    val profileScreenData: Flow<ProfileScreenData>

    /**
     * Suspend function to sign the user out.
     *
     * @return A [Result] representing the sign-out operation result. It contains [Unit] if
     * the sign-out was successful, or an error if there was a problem.
     */
    suspend fun signOut(): Result<Unit>
}
