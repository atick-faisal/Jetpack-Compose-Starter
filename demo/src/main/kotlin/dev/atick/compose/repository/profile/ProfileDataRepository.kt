/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
