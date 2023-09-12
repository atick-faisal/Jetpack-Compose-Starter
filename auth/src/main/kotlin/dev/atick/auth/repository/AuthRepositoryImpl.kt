package dev.atick.auth.repository

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.data.AuthDataSource
import dev.atick.auth.model.AuthUser
import dev.atick.storage.preferences.data.UserPreferencesDataSource
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface responsible for handling authentication operations.
 *
 * @param authDataSource The data source for authentication operations.
 * @param userPreferencesDataSource The data source for user preferences.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : AuthRepository {
    /**
     * Retrieves an [IntentSender] for initiating Google Sign-In.
     *
     * @return A [Result] containing the [IntentSender] for Google Sign-In.
     */
    override suspend fun getGoogleSignInIntent(): Result<IntentSender> {
        return runCatching {
            authDataSource.getGoogleSignInIntent()
                ?: throw Exception("Unable not Sign In with Google")
        }
    }

    /**
     * Sign in using an [Intent] obtained from Google Sign-In.
     *
     * @param intent The [Intent] obtained from Google Sign-In.
     * @return A [Result] containing the authenticated [AuthUser] upon successful sign-in.
     */
    override suspend fun signInWithIntent(intent: Intent): Result<AuthUser> {
        return runCatching {
            val user = authDataSource.signInWithIntent(intent)
            userPreferencesDataSource.setProfile(user.asProfile())
            user
        }
    }

    /**
     * Sign in with an email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] containing the authenticated [AuthUser] upon successful sign-in.
     */
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<AuthUser> {
        return runCatching {
            val user = authDataSource.signInWithEmailAndPassword(email, password)
            userPreferencesDataSource.setProfile(user.asProfile())
            user
        }
    }

    /**
     * Register a new user with an email and password.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] containing the authenticated [AuthUser] upon successful registration.
     */
    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): Result<AuthUser> {
        return runCatching {
            val user = authDataSource.registerWithEmailAndPassword(name, email, password)
            userPreferencesDataSource.setProfile(user.asProfile())
            user
        }
    }
}
