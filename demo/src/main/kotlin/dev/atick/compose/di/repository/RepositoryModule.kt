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

package dev.atick.compose.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.compose.repository.home.PostsRepository
import dev.atick.compose.repository.home.PostsRepositoryImpl
import dev.atick.compose.repository.profile.ProfileDataRepository
import dev.atick.compose.repository.profile.ProfileDataRepositoryImpl
import dev.atick.compose.repository.user.UserDataRepository
import dev.atick.compose.repository.user.UserDataRepositoryImpl
import javax.inject.Singleton

/**
 * Dagger module that provides the binding for the [PostsRepository] interface.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [PostsRepositoryImpl] implementation to the [PostsRepository] interface.
     *
     * @param postsRepositoryImpl The implementation of [PostsRepository] to be bound.
     * @return The [PostsRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindPostsRepository(
        postsRepositoryImpl: PostsRepositoryImpl,
    ): PostsRepository

    /**
     * Binds the [UserDataRepositoryImpl] implementation to the [UserDataRepository] interface.
     *
     * @param userDataRepositoryImpl The implementation of [UserDataRepository] to be bound.
     * @return The [UserDataRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl,
    ): UserDataRepository

    /**
     * This method is used to bind a [ProfileDataRepositoryImpl] instance to the [ProfileDataRepository] interface.
     * It is annotated with [@Binds](https://developer.android.com/reference/dagger/Binds) and [@Singleton](https://developer.android.com/reference/javax/inject/Singleton), indicating that a single instance
     * of [ProfileDataRepositoryImpl] should be used as the implementation of [ProfileDataRepository] throughout the application.
     *
     * @param profileDataRepositoryImpl The [ProfileDataRepositoryImpl] instance to be bound to [ProfileDataRepository].
     * @return An instance of [ProfileDataRepository] representing the bound implementation.
     */
    @Binds
    @Singleton
    abstract fun bindProfileDataRepository(
        profileDataRepositoryImpl: ProfileDataRepositoryImpl,
    ): ProfileDataRepository
}
