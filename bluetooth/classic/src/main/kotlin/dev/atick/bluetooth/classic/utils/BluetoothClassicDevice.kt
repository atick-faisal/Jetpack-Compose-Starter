package dev.atick.bluetooth.classic.utils

import android.bluetooth.BluetoothDevice

class BluetoothClassicDevice(
    device: BluetoothDevice,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
) {
    val name: String = try {
        device.name ?: "Unknown"
    } catch (e: SecurityException) {
        "Requires Permission"
    }

    val address = device.address ?: "Unknown"
}