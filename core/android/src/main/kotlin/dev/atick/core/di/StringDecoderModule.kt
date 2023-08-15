package dev.atick.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.core.utils.StringDecoder
import dev.atick.core.utils.UriDecoder

@Module
@InstallIn(SingletonComponent::class)
abstract class StringDecoderModule {
    @Binds
    abstract fun bindStringDecoder(uriDecoder: UriDecoder): StringDecoder
}