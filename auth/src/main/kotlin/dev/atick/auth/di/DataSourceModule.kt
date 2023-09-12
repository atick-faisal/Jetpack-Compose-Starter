package dev.atick.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.auth.data.AuthDataSource
import dev.atick.auth.data.AuthDataSourceImpl
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing data source dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    /**
     * Binds the [AuthDataSourceImpl] implementation to the [AuthDataSource] interface.
     *
     * @param authDataSourceImpl The implementation of [AuthDataSource] to be bound.
     * @return An instance of [AuthDataSource] for dependency injection.
     */
    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

}
