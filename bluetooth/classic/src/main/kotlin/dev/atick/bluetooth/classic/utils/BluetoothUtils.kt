package dev.atick.bluetooth.classic.utils

import kotlinx.coroutines.flow.StateFlow

interface BluetoothUtils {
    val bluetoothState: StateFlow<BluetoothState>
    fun getScannedDevices(): StateFlow<List<BluetoothClassicDevice>>
    fun getPairedDevices(): StateFlow<List<BluetoothClassicDevice>>
    fun startDiscovery()
    fun stopDiscovery()
}