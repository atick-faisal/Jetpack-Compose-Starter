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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val signInClient: SignInClient,
) : AuthDataSource {

    override val currentUser: AuthUser?
        get() = firebaseAuth.currentUser?.run { asAuthUser() }


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<AuthUser> {
        return runCatching {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user!!
            user.asAuthUser()
        }
    }

    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
    ): Result<AuthUser> {
        return runCatching {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user!!
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                .await()
            user.asAuthUser()
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getGoogleSignInIntent(): IntentSender? {
        val result = try {
            signInClient.beginSignIn(
                buildSignInRequest(),
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): Result<AuthUser> {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return runCatching {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user!!
            AuthUser(
                id = user.uid,
                name = user.displayName,
                profilePictureUri = user.photoUrl,
            )
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