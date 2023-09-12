package dev.atick.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.auth.repository.AuthRepository
import dev.atick.auth.repository.AuthRepositoryImpl
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing repository dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [AuthRepositoryImpl] implementation to the [AuthRepository] interface.
     *
     * @param authRepositoryImpl The implementation of [AuthRepository] to be bound.
     * @return An instance of [AuthRepository] for dependency injection.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository
}
