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

@file:Suppress("DEPRECATION")

package dev.atick.bluetooth.common.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.bluetooth.common.models.simplify

class DeviceStateReceiver(
    private val onConnectionStateChange: (BtDevice) -> Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java,
            )
        } else {
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        if (device == null) return
        when (intent?.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onConnectionStateChange(device.simplify(true))
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                onConnectionStateChange(device.simplify(false))
            }
        }
    }
}
