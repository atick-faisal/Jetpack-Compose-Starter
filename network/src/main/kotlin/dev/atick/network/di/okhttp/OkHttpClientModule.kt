package dev.atick.network.di.okhttp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module(
    includes = [
        InterceptorModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    private const val TIME_OUT = 60L

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, SECONDS)
            .readTimeout(TIME_OUT, SECONDS)
            .writeTimeout(TIME_OUT, SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

}