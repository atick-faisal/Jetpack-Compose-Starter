package dev.atick.bluetooth.common.data

import dev.atick.bluetooth.common.models.BtMessage
import kotlinx.coroutines.flow.Flow

interface BluetoothDataSource {
    fun getBluetoothDataStream(): Flow<BtMessage>
    suspend fun sendDataToBluetoothDevice(data: CharSequence): Result<Unit>
}