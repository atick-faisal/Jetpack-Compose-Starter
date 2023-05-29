package dev.atick.bluetooth.common.utils

import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.bluetooth.common.models.BtState
import kotlinx.coroutines.flow.StateFlow

interface BluetoothUtils {
    fun getBluetoothState(): StateFlow<BtState>
    fun getScannedDevices(): StateFlow<List<BtDevice>>
    fun getPairedDevices(): StateFlow<List<BtDevice>>
    fun stopDiscovery()
}