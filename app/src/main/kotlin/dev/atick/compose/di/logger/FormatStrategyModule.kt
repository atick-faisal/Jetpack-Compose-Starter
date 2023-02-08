package dev.atick.compose.di.logger

import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FormatStrategyModule {

    private const val LOG_TAG = "SKYNET_LOG"

    @Provides
    @Singleton
    fun provideFormatStrategy(): FormatStrategy {
        return PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .tag(LOG_TAG)
            .build()
    }
}