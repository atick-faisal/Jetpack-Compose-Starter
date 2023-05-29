package dev.atick.bluetooth.classic.data

import dev.atick.bluetooth.classic.data.models.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothDataSource {
    val bluetoothDataStream: StateFlow<BluetoothMessage?>
    suspend fun sendDataToBluetoothDevice(data: String): Result<Unit>
}