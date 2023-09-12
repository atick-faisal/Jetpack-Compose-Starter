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

package dev.atick.bluetooth.common.utils

import dev.atick.bluetooth.common.model.BtDevice
import dev.atick.bluetooth.common.model.BtState
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining Bluetooth utility methods.
 */
interface BluetoothUtils {
    /**
     * Retrieves the current state of Bluetooth.
     *
     * @return A [StateFlow] emitting the current Bluetooth state.
     */
    fun getBluetoothState(): StateFlow<BtState>

    /**
     * Retrieves the list of scanned Bluetooth devices.
     *
     * @return A [StateFlow] emitting the list of scanned Bluetooth devices.
     */
    fun getScannedDevices(): StateFlow<List<BtDevice>>

    /**
     * Retrieves the list of paired Bluetooth devices.
     *
     * @return A [StateFlow] emitting the list of paired Bluetooth devices.
     */
    fun getPairedDevices(): StateFlow<List<BtDevice>>

    /**
     * Stops the Bluetooth device discovery process.
     */
    fun stopDiscovery()
}
