@file:Suppress("DEPRECATION")

package dev.atick.bluetooth.classic.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.atick.bluetooth.classic.utils.BluetoothClassicDevice
import dev.atick.bluetooth.classic.utils.ConnectionState

class DeviceStateReceiver(
    private val deviceAddress: String,
    private val onConnectionStateChange: (BluetoothClassicDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        when (intent?.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                device?.run {
                    BluetoothClassicDevice(
                        device = this,
                        connectionState = ConnectionState.CONNECTED
                    )
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                device?.run {
                    BluetoothClassicDevice(
                        device = this,
                        connectionState = ConnectionState.DISCONNECTED
                    )
                }
            }
        }
    }
}