package dev.atick.network.di.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Module for providing [Converter.Factory].
 */
@Module
@InstallIn(SingletonComponent::class)
object ConverterModule {
    /**
     * Provides kotlinx.serialization [Converter.Factory].
     *
     * @return [Converter.Factory].
     */
    @Provides
    @Singleton
    fun provideConverter(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.asConverterFactory(
            "application/json; charset=UTF8".toMediaType(),
        )
    }
}