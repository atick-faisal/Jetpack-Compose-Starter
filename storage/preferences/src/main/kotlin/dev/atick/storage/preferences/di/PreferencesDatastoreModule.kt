package dev.atick.storage.preferences.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.storage.preferences.data.PreferencesDatastore
import dev.atick.storage.preferences.data.PreferencesDatastoreImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesDatastoreModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesDatastore(
        preferencesDatastoreImpl: PreferencesDatastoreImpl
    ): PreferencesDatastore
}