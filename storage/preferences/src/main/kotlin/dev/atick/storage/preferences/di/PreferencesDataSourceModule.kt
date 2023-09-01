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

package dev.atick.storage.preferences.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.storage.preferences.UserPreferencesDataSource
import dev.atick.storage.preferences.data.UserPreferencesDataSourceImpl
import javax.inject.Singleton

/**
 * Preferences DataSource module
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesDataSourceModule {

    /**
     * Bind preferences datasource
     *
     * @param userPreferencesDataSourceImpl PreferencesDatastoreImpl
     * @return [UserPreferencesDataSource]
     */
    @Binds
    @Singleton
    abstract fun bindUserPreferencesDataSource(
        userPreferencesDataSourceImpl: UserPreferencesDataSourceImpl,
    ): UserPreferencesDataSource
}
