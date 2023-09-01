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

package dev.atick.bluetooth.common.manager

import dev.atick.bluetooth.common.models.BtDevice
import kotlinx.coroutines.flow.StateFlow

/**
 * BluetoothManager interface provides methods to manage Bluetooth connections.
 */
interface BluetoothManager {
    /**
     * Attempts to establish a Bluetooth connection with the specified device address.
     *
     * @param address The address of the Bluetooth device to connect to.
     * @return A [Result] indicating the success or failure of the connection attempt.
     */
    suspend fun connect(address: String): Result<Unit>

    /**
     * Returns the state of the connected Bluetooth device.
     *
     * @return A [StateFlow] emitting the current state of the connected Bluetooth device.
     */
    fun getConnectedDeviceState(): StateFlow<BtDevice?>

    /**
     * Closes the existing Bluetooth connection.
     *
     * @return A [Result] indicating the success or failure of closing the connection.
     */
    suspend fun closeConnection(): Result<Unit>
}
