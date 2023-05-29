@file:Suppress("DEPRECATION")

package dev.atick.bluetooth.classic.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.atick.bluetooth.classic.utils.BluetoothClassicDevice

class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothClassicDevice) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                device?.run { onDeviceFound(BluetoothClassicDevice(this)) }
            }
        }
    }
}