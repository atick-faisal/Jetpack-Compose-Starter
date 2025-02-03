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

package dev.atick.network.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing [ConnectivityManager].
 */
@Module
@InstallIn(SingletonComponent::class)
object ConnectivityManagerModule {

    /**
     * Provides [ConnectivityManager].
     *
     * @param context [Context].
     * @return [ConnectivityManager].
     */
    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
    ): ConnectivityManager {
        return context.getSystemService(
            Context.CONNECTIVITY_SERVICE,
        ) as ConnectivityManager
    }
}
