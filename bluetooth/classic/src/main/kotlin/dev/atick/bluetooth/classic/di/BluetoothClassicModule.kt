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

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothClassicModule {

    @Binds
    @Singleton
    abstract fun provideBluetoothUtils(
        bluetoothClassic: BluetoothClassic
    ): BluetoothUtils

    @Binds
    @Singleton
    abstract fun bindBluetoothManager(
        bluetoothClassic: BluetoothClassic
    ): BluetoothManager

    @Binds
    @Singleton
    abstract fun bindBluetoothDataSource(
        bluetoothClassic: BluetoothClassic
    ): BluetoothDataSource

}