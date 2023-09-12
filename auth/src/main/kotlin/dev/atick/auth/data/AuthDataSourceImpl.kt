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

class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val signInClient: SignInClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthDataSource {

    override val currentUser: AuthUser?
        get() = firebaseAuth.currentUser?.run { asAuthUser() }


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): AuthUser {
        return withContext(ioDispatcher) {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user!!
            user.asAuthUser()
        }
    }

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

    override suspend fun signOut() {
        withContext(ioDispatcher) {
            firebaseAuth.signOut()
        }
    }

    override suspend fun getGoogleSignInIntent(): IntentSender? {
        return withContext(ioDispatcher) {
            val result = signInClient.beginSignIn(buildSignInRequest()).await()
            result?.pendingIntent?.intentSender
        }
    }

    override suspend fun signInWithIntent(intent: Intent): AuthUser {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return withContext(ioDispatcher) {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user!!
            user.asAuthUser()
        }
    }

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