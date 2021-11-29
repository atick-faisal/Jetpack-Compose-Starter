package dev.atick.core.di.logger

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
    private const val LOG_METHOD_COUNT = 2
    private const val LOG_METHOD_OFFSET = 7

    @Provides
    @Singleton
    fun provideFormatStrategy(): FormatStrategy {
        return PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(LOG_METHOD_COUNT)
            .methodOffset(LOG_METHOD_OFFSET)
            .tag(LOG_TAG)
            .build()
    }
}