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

package dev.atick.auth.data

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.models.AuthUser

/**
 * Interface defining data source operations for authentication.
 */
interface AuthDataSource {
    /**
     * Retrieves an [IntentSender] for initiating Google Sign-In.
     *
     * @return The [IntentSender] for Google Sign-In, or null if unavailable.
     */
    suspend fun getGoogleSignInIntent(): IntentSender?

    /**
     * Sign in using an [Intent] obtained from Google Sign-In.
     *
     * @param intent The [Intent] obtained from Google Sign-In.
     * @return The authenticated [AuthUser] upon successful sign-in.
     */
    suspend fun signInWithIntent(intent: Intent): AuthUser

    /**
     * Gets the currently authenticated user, if any.
     *
     * @return The currently authenticated [AuthUser], or null if not signed in.
     */
    val currentUser: AuthUser?

    /**
     * Sign in with an email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated [AuthUser] upon successful sign-in.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthUser

    /**
     * Register a new user with an email and password.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated [AuthUser] upon successful registration.
     */
    suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): AuthUser

    /**
     * Sign out the currently authenticated user.
     */
    suspend fun signOut()
}
