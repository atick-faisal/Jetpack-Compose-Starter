package dev.atick.storage.room.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.storage.room.data.JetpackDatabase
import javax.inject.Singleton

@Module(
    includes = [
        DatabaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun provideJetpackDao(jetpackDatabase: JetpackDatabase) = jetpackDatabase.getJetpackDao()
}