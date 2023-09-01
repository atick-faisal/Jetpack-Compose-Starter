/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Dagger module that provides coroutine dispatchers for different contexts.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    /**
     * Provides the default coroutine dispatcher, which is used for general-purpose background tasks.
     *
     * @return The default coroutine dispatcher.
     */
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides the I/O coroutine dispatcher, which is used for I/O-bound tasks such as disk or network operations.
     *
     * @return The I/O coroutine dispatcher.
     */
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides the main coroutine dispatcher, which is used for executing tasks on the main/UI thread.
     *
     * @return The main coroutine dispatcher.
     */
    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

/**
 * Annotation used to mark the default coroutine dispatcher.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

/**
 * Annotation used to mark the I/O coroutine dispatcher.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

/**
 * Annotation used to mark the main coroutine dispatcher.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
