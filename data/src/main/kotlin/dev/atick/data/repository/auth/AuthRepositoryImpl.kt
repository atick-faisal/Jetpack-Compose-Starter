/*
 * Copyright 2025 Atick Faisal
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

package dev.atick.data.repository.auth

import android.app.Activity
import dev.atick.core.preferences.data.UserPreferencesDataSource
import dev.atick.core.preferences.model.PreferencesUserProfile
import dev.atick.core.utils.suspendRunCatching
import dev.atick.firebase.auth.data.AuthDataSource
import dev.atick.firebase.auth.model.AuthUser
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface responsible for handling authentication operations.
 *
 * @param authDataSource The data source for authentication operations.
 * @param userPreferencesDataSource The data source for user preferences.
 */
internal class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : AuthRepository {

    /**
     * Sign in with saved credentials.
     *
     * @param activity The activity instance.
     * @return A [Result] representing the sign-in operation result. It contains [Unit] if
     * the sign-in was successful, or an error if there was a problem.
     */
    override suspend fun signInWithSavedCredentials(activity: Activity): Result<Unit> {
        return suspendRunCatching {
            val user = authDataSource.signInWithSavedCredentials(activity)
            userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
        }
    }

    /**
     * Sign in with an email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] representing the sign-in operation result. It contains [Unit] if
     * the sign-in was successful, or an error if there was a problem.
     */
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return suspendRunCatching {
            val user = authDataSource.signInWithEmailAndPassword(email, password)
            userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
        }
    }

    /**
     * Register a new user with an email and password.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] representing the registration operation result. It contains [Unit] if
     * the registration was successful, or an error if there was a problem.
     */
    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity,
    ): Result<Unit> {
        return suspendRunCatching {
            val user = authDataSource.registerWithEmailAndPassword(name, email, password, activity)
            userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
        }
    }

    /**
     * Sign in with Google.
     *
     * @param activity The current activity.
     * @return A [Result] representing the sign-in operation result. It contains [Unit] if
     * the sign-in was successful, or an error if there was a problem.
     */
    override suspend fun signInWithGoogle(activity: Activity): Result<Unit> {
        return suspendRunCatching {
            val user = authDataSource.signInWithGoogle(activity)
            userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
        }
    }

    /**
     * Register a new user with Google.
     *
     * @param activity The current activity.
     * @return A [Result] representing the registration operation result. It contains [Unit] if
     * the registration was successful, or an error if there was a problem.
     */
    override suspend fun registerWithGoogle(activity: Activity): Result<Unit> {
        return suspendRunCatching {
            val user = authDataSource.registerWithGoogle(activity)
            userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
        }
    }

    /**
     * Convert an [AuthUser] to a [PreferencesUserProfile].
     */
    private fun AuthUser.asPreferencesUserProfile() = PreferencesUserProfile(
        id = id,
        userName = name,
        profilePictureUriString = profilePictureUri?.toString(),
    )
}
