package dev.atick.compose.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.compose.repository.home.JetpackRepository
import dev.atick.compose.repository.home.JetpackRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJetpackRepository(
        jetpackRepositoryImpl: JetpackRepositoryImpl
    ): JetpackRepository
}