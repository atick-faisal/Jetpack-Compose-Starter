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

package dev.atick.core.room.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.core.room.data.JetpackDatabase
import javax.inject.Singleton

/**
 * Dagger module for data access object.
 */
@Module(
    includes = [
        DatabaseModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object DaoModule {

    /**
     * Get the data access.
     *
     * @param jetpackDatabase The database for Jetpack.
     * @return The data access object.
     */
    @Singleton
    @Provides
    fun provideJetpackDao(jetpackDatabase: JetpackDatabase) = jetpackDatabase.getJetpackDao()
}
