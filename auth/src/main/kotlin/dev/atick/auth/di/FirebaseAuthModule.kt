package dev.atick.auth.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing Firebase Authentication-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseAuthModule {

    /**
     * Provides a singleton instance of [FirebaseAuth].
     *
     * @return An instance of [FirebaseAuth] for authentication operations.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    /**
     * Provides a singleton instance of [SignInClient] for identity-related operations.
     *
     * @param context The application context.
     * @return An instance of [SignInClient] for handling identity operations.
     */
    @Provides
    @Singleton
    fun provideSingInClient(
        @ApplicationContext context: Context,
    ): SignInClient {
        return Identity.getSignInClient(context)
    }
}