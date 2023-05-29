package dev.atick.bluetooth.classic.manager

import dev.atick.bluetooth.classic.utils.BluetoothClassicDevice
import kotlinx.coroutines.flow.StateFlow

interface BluetoothManager {
    suspend fun connect()
    fun getCurrentDevice(): StateFlow<BluetoothClassicDevice?>
    fun disconnect()
}