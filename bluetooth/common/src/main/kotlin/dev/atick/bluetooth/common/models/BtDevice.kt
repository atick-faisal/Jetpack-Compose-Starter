package dev.atick.bluetooth.common.models

import android.bluetooth.BluetoothClass.Device.Major.AUDIO_VIDEO
import android.bluetooth.BluetoothClass.Device.Major.COMPUTER
import android.bluetooth.BluetoothClass.Device.Major.HEALTH
import android.bluetooth.BluetoothClass.Device.Major.IMAGING
import android.bluetooth.BluetoothClass.Device.Major.MISC
import android.bluetooth.BluetoothClass.Device.Major.NETWORKING
import android.bluetooth.BluetoothClass.Device.Major.PERIPHERAL
import android.bluetooth.BluetoothClass.Device.Major.PHONE
import android.bluetooth.BluetoothClass.Device.Major.TOY
import android.bluetooth.BluetoothClass.Device.Major.UNCATEGORIZED
import android.bluetooth.BluetoothClass.Device.Major.WEARABLE
import android.bluetooth.BluetoothDevice

data class BtDevice(
    val name: String,
    val address: String,
    val type: BtDeviceType,
    val connected: Boolean
)

fun BluetoothDevice.simplify(
    connected: Boolean = false
): BtDevice {
    val simpleName = try {
        name ?: "Unknown"
    } catch (e: SecurityException) {
        "Permission Required"
    }

    val simpleAddress = address ?: "Unknown"

    val simpleType = try {
        when (bluetoothClass.majorDeviceClass) {
            AUDIO_VIDEO -> BtDeviceType.AUDIO_VIDEO
            COMPUTER -> BtDeviceType.COMPUTER
            HEALTH -> BtDeviceType.HEALTH
            IMAGING -> BtDeviceType.IMAGING
            MISC -> BtDeviceType.MISC
            NETWORKING -> BtDeviceType.NETWORKING
            PERIPHERAL -> BtDeviceType.PERIPHERAL
            PHONE -> BtDeviceType.PHONE
            TOY -> BtDeviceType.TOY
            UNCATEGORIZED -> BtDeviceType.UNCATEGORIZED
            WEARABLE -> BtDeviceType.WEARABLE
            else -> BtDeviceType.UNCATEGORIZED
        }

    } catch (e: SecurityException) {
        BtDeviceType.UNCATEGORIZED
    }

    return BtDevice(
        name = simpleName,
        address = simpleAddress,
        type = simpleType,
        connected = connected
    )
}