/*
 * Copyright 2025 Atick Faisal
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

package dev.atick.firebase.firestore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.firebase.firestore.data.FirebaseDataSource
import dev.atick.firebase.firestore.data.FirebaseDataSourceImpl
import javax.inject.Singleton

/**
 * Dagger module to provide Firebase data source dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    /**
     * Binds the implementation of [FirebaseDataSource] to [FirebaseDataSourceImpl].
     *
     * @param firebaseDataSourceImpl The implementation of [FirebaseDataSource].
     * @return The bound [FirebaseDataSource] instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindFirebaseDataSource(
        firebaseDataSourceImpl: FirebaseDataSourceImpl,
    ): FirebaseDataSource
}
