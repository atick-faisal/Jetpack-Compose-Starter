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
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dev.atick.auth.config.Config
import dev.atick.auth.model.AuthUser
import dev.atick.auth.model.asAuthUser
import dev.atick.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [AuthDataSource] interface responsible for handling authentication data operations.
 *
 * @param firebaseAuth The Firebase Authentication instance for performing authentication operations.
 * @param signInClient The client for handling identity-related operations.
 * @param ioDispatcher The [CoroutineDispatcher] for executing suspend functions in an IO context.
 */
class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val signInClient: SignInClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthDataSource {

    /**
     * Gets the currently authenticated user, if any.
     *
     * @return The currently authenticated [AuthUser], or null if not signed in.
     */
    override val currentUser: AuthUser?
        get() = firebaseAuth.currentUser?.run { asAuthUser() }

    /**
     * Sign in with an email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated [AuthUser] upon successful sign-in.
     */
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): AuthUser {
        return withContext(ioDispatcher) {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user!!
            user.asAuthUser()
        }
    }

    /**
     * Register a new user with an email and password.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated [AuthUser] upon successful registration.
     */
    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): AuthUser {
        return withContext(ioDispatcher) {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user!!
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                .await()
            user.asAuthUser()
        }
    }

    /**
     * Sign out the currently authenticated user.
     */
    override suspend fun signOut() {
        withContext(ioDispatcher) {
            firebaseAuth.signOut()
        }
    }

    /**
     * Retrieves an [IntentSender] for initiating Google Sign-In.
     *
     * @return The [IntentSender] for Google Sign-In, or null if unavailable.
     */
    override suspend fun getGoogleSignInIntent(): IntentSender? {
        return withContext(ioDispatcher) {
            val result = signInClient.beginSignIn(buildSignInRequest()).await()
            result?.pendingIntent?.intentSender
        }
    }

    /**
     * Sign in using an [Intent] obtained from Google Sign-In.
     *
     * @param intent The [Intent] obtained from Google Sign-In.
     * @return The authenticated [AuthUser] upon successful sign-in.
     */
    override suspend fun signInWithIntent(intent: Intent): AuthUser {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return withContext(ioDispatcher) {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user!!
            user.asAuthUser()
        }
    }

    /**
     * Builds a [BeginSignInRequest] for Google Sign-In.
     *
     * @return The constructed [BeginSignInRequest].
     */
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(Config.WEB_CLIENT_ID)
                    .build(),
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}
