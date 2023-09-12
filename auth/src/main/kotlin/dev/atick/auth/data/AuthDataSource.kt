package dev.atick.auth.data

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.model.AuthUser

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
