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

package dev.atick.core.preferences.data

import androidx.datastore.core.DataStore
import dev.atick.core.di.IoDispatcher
import dev.atick.core.preferences.model.DarkThemeConfigPreferences
import dev.atick.core.preferences.model.PreferencesUserProfile
import dev.atick.core.preferences.model.UserDataPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [UserPreferencesDataSource] interface using DataStore to manage user preferences.
 *
 * @property datastore The DataStore instance to manage user preferences data.
 * @property ioDispatcher The CoroutineDispatcher for performing I/O operations.
 */
internal class UserPreferencesDataSourceImpl @Inject constructor(
    private val datastore: DataStore<UserDataPreferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserPreferencesDataSource {

    /**
     * Retrieves the user profile from the user preferences.
     *
     * @return A [Flow] of [UserDataPreferences].
     */
    override fun getUserDataPreferences(): Flow<UserDataPreferences> =
        datastore.data.flowOn(ioDispatcher)

    /**
     * Retrieves the user ID or throws an exception if the user is not authenticated.
     *
     * @return The user ID as a [String].
     * @throws IllegalStateException if the user is not authenticated.
     */
    override suspend fun getUserIdOrThrow(): String {
        return withContext(ioDispatcher) {
            val userId = datastore.data.first().id
            if (userId.isEmpty()) throw IllegalStateException("User not authenticated")
            userId
        }
    }

    /**
     * Sets the user profile in the user preferences.
     *
     * @param preferencesUserProfile The user [PreferencesUserProfile] to be set.
     */
    override suspend fun setUserProfile(preferencesUserProfile: PreferencesUserProfile) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(
                    id = preferencesUserProfile.id,
                    userName = preferencesUserProfile.userName,
                    profilePictureUriString = preferencesUserProfile.profilePictureUriString,
                )
            }
        }
    }

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfigPreferences The dark theme configuration to be set.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfigPreferences: DarkThemeConfigPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(darkThemeConfigPreferences = darkThemeConfigPreferences)
            }
        }
    }

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(useDynamicColor = useDynamicColor)
            }
        }
    }

    /**
     * Resets the user preferences to their default values.
     */
    override suspend fun resetUserPreferences() {
        withContext(ioDispatcher) {
            datastore.updateData { UserDataPreferences() }
        }
    }
}
