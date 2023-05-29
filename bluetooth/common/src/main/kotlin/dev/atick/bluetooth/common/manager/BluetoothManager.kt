package dev.atick.bluetooth.common.manager

import dev.atick.bluetooth.common.models.BtDevice
import kotlinx.coroutines.flow.StateFlow

interface BluetoothManager {
    suspend fun connect(): Result<Unit>
    fun getConnectedDeviceState(): StateFlow<BtDevice?>
    suspend fun closeConnection()
}