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

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dev.atick.auth.config.Config
import dev.atick.auth.models.AuthUser
import dev.atick.auth.models.asAuthUser
import dev.atick.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [AuthDataSource] interface responsible for handling authentication data operations.
 *
 * @param firebaseAuth The Firebase Authentication instance for performing authentication operations.
 * @param credentialManager The [CredentialManager] for handling credential operations.
 * @param ioDispatcher The [CoroutineDispatcher] for executing suspend functions in an IO context.
 */
class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
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
     * Sign in with Google.
     *
     * @param activity The activity context.
     * @return The authenticated [AuthUser] upon successful sign-in.
     */
    override suspend fun signInWithGoogle(activity: Activity): AuthUser {
        val request = getSignInWithGoogleRequest()
        return withContext(ioDispatcher) {
            val result = credentialManager.getCredential(
                request = request,
                context = activity,
            )
            handleGoogleAuthResult(result)
        }
    }

    /**
     * Register with Google.
     *
     * @param activity The activity context.
     * @return The authenticated [AuthUser] upon successful registration.
     */
    override suspend fun registerWithGoogle(activity: Activity): AuthUser {
        val request = registerWithGoogleRequest()
        return withContext(ioDispatcher) {
            val result = credentialManager.getCredential(
                request = request,
                context = activity,
            )
            handleGoogleAuthResult(result)
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
     * Get the sign-in request for Google.
     *
     * @return The [GetCredentialRequest] for Google sign-in.
     */
    private fun getSignInWithGoogleRequest(): GetCredentialRequest {
        val signInRequestOptions = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
            .setServerClientId(Config.WEB_CLIENT_ID).setAutoSelectEnabled(true).build()
        return GetCredentialRequest.Builder().addCredentialOption(signInRequestOptions).build()
    }

    /**
     * Get the registration request for Google.
     *
     * @return The [GetCredentialRequest] for Google registration.
     */
    private fun registerWithGoogleRequest(): GetCredentialRequest {
        val signInRequestOptions = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
            .setServerClientId(Config.WEB_CLIENT_ID).setAutoSelectEnabled(false).build()
        return GetCredentialRequest.Builder().addCredentialOption(signInRequestOptions).build()
    }

    /**
     * Handle the result of Google authentication.
     *
     * @param result The result of Google authentication.
     * @return The authenticated [AuthUser] upon successful authentication.
     */
    private suspend fun handleGoogleAuthResult(result: GetCredentialResponse): AuthUser {
        if (result.credential !is CustomCredential) throw Exception("Something went wrong when signing in with Google")

        val credential = result.credential as CustomCredential

        if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            throw Exception(
                "Something went wrong when signing in with Google",
            )
        }

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val googleCredentials =
            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

        val user = firebaseAuth.signInWithCredential(googleCredentials).await().user!!
        return user.asAuthUser()
    }
}
