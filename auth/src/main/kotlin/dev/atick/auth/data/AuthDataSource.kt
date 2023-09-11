package dev.atick.auth.data

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.model.AuthUser

interface AuthDataSource {
    suspend fun getGoogleSignInIntent(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): AuthUser

    val currentUser: AuthUser?
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthUser
    suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): AuthUser

    suspend fun logout()
}