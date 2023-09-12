package dev.atick.auth.repository

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.data.AuthDataSource
import dev.atick.auth.model.AuthUser
import dev.atick.storage.preferences.UserPreferencesDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,

    ) : AuthRepository {
    override suspend fun getGoogleSignInIntent(): Result<IntentSender> {
        return runCatching {
            authDataSource.getGoogleSignInIntent()
                ?: throw Exception("Unable not Sign In with Google")
        }
    }

    override suspend fun signInWithIntent(intent: Intent): Result<AuthUser> {
        return runCatching {
            val user = authDataSource.signInWithIntent(intent)
            userPreferencesDataSource.setProfile(user.asProfile())
            user
        }
    }

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