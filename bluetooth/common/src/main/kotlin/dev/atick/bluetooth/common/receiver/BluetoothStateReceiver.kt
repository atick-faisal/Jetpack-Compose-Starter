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

package dev.atick.bluetooth.common.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.atick.bluetooth.common.models.BtState

/**
 * BroadcastReceiver for receiving Bluetooth state changes.
 *
 * @param onBtStateChange Callback function to handle Bluetooth state changes.
 */
class BluetoothStateReceiver(
    private val onBtStateChange: (BtState) -> Unit,
) : BroadcastReceiver() {

    /**
     * Called when a broadcast is received.
     *
     * @param context The context of the receiver.
     * @param intent The intent containing the broadcast information.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_ON -> onBtStateChange(BtState.ENABLED)
            BluetoothAdapter.STATE_OFF -> onBtStateChange(BtState.DISABLED)
        }
    }
}
