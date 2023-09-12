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

package dev.atick.bluetooth.common.data

import dev.atick.bluetooth.common.model.BtMessage
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface representing a Bluetooth data source.
 */
interface BluetoothDataSource {
    /**
     * Returns the state flow of Bluetooth messages received from the connected device.
     *
     * @return The state flow of Bluetooth messages.
     */
    fun getBluetoothDataStream(): StateFlow<BtMessage?>

    /**
     * Sends data to the connected Bluetooth device.
     *
     * @param data The data to send.
     * @return A result indicating the success or failure of sending the data.
     */
    suspend fun sendDataToBluetoothDevice(data: String): Result<Unit>
}
