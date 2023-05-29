package dev.atick.bluetooth.common.data

import dev.atick.bluetooth.common.models.BtMessage
import kotlinx.coroutines.flow.StateFlow

interface BluetoothDataSource {
    fun getBluetoothDataStream(): StateFlow<BtMessage?>
    suspend fun sendDataToBluetoothDevice(data: CharSequence): Result<Unit>
}