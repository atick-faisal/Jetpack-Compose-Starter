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

package dev.atick.bluetooth.classic.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.bluetooth.classic.BluetoothClassic
import dev.atick.bluetooth.common.data.BluetoothDataSource
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.bluetooth.common.utils.BluetoothUtils
import javax.inject.Singleton

/**
 * Dagger module for providing Bluetooth Classic related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothClassicModule {

    /**
     * Binds [BluetoothClassic] implementation to [BluetoothUtils] interface.
     *
     * @param bluetoothClassic The Bluetooth Classic implementation.
     * @return The BluetoothUtils interface.
     */
    @Binds
    @Singleton
    abstract fun provideBluetoothUtils(
        bluetoothClassic: BluetoothClassic,
    ): BluetoothUtils

    /**
     * Binds [BluetoothClassic] implementation to [BluetoothManager] interface.
     *
     * @param bluetoothClassic The Bluetooth Classic implementation.
     * @return The BluetoothManager interface.
     */
    @Binds
    @Singleton
    abstract fun bindBluetoothManager(
        bluetoothClassic: BluetoothClassic,
    ): BluetoothManager

    /**
     * Binds [BluetoothClassic] implementation to [BluetoothDataSource] interface.
     *
     * @param bluetoothClassic The Bluetooth Classic implementation.
     * @return The BluetoothDataSource interface.
     */
    @Binds
    @Singleton
    abstract fun bindBluetoothDataSource(
        bluetoothClassic: BluetoothClassic,
    ): BluetoothDataSource
}
