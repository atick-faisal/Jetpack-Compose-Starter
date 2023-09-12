package dev.atick.auth.repository

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.model.AuthUser

interface AuthRepository {
    suspend fun getGoogleSignInIntent(): Result<IntentSender>
    suspend fun signInWithIntent(intent: Intent): Result<AuthUser>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<AuthUser>
    suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): Result<AuthUser>
}