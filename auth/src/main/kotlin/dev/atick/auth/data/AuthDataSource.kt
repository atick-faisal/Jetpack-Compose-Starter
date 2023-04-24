package dev.atick.auth.data

import android.content.Intent
import android.content.IntentSender
import dev.atick.auth.data.models.User

interface AuthDataSource {
    suspend fun getGoogleSignInIntent(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): Result<User>
}