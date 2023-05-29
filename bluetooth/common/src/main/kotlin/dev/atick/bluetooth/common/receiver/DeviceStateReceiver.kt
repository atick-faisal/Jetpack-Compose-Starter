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
    private val deviceAddress: String,
    private val onConnectionStateChange: (BtDevice) -> Unit
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
        if (device?.address != deviceAddress) return
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