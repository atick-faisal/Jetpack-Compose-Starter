package dev.atick.storage.room.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.storage.room.LocalDataSource
import dev.atick.storage.room.data.LocalDataSourceImpl
import javax.inject.Singleton

/**
 * Dagger Hilt module responsible for providing implementations of data source interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    /**
     * Binds the [LocalDataSourceImpl] implementation to the [LocalDataSource] interface.
     *
     * @param localDataSourceImpl The concrete implementation of [LocalDataSourceImpl].
     * @return An instance of [LocalDataSource] representing the local data source.
     */
    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

}
