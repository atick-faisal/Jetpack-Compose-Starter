package dev.atick.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.network.api.ApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [
        RetrofitModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}