package dev.atick.bluetooth.classic

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.bluetooth.common.models.BtState
import dev.atick.bluetooth.common.models.simplify
import dev.atick.bluetooth.common.receiver.BluetoothStateReceiver
import dev.atick.bluetooth.common.receiver.ScannedDeviceReceiver
import dev.atick.bluetooth.common.utils.BluetoothUtils
import dev.atick.core.extensions.hasPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class BluetoothClassic @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BluetoothUtils {

    private val _bluetoothState = MutableStateFlow(
        if (bluetoothAdapter?.isEnabled == true) BtState.ENABLED
        else BtState.DISABLED
    )

    private val _scannedDevices = MutableStateFlow(emptyList<BtDevice>())
    private val _pairedDevices = MutableStateFlow(emptyList<BtDevice>())

    private val bluetoothStateReceiver = BluetoothStateReceiver { state ->
        Timber.i("BT STATE UPDATE: $state")
        _bluetoothState.update { state }
    }

    private val scannedDeviceReceiver = ScannedDeviceReceiver { device ->
        if (device in _scannedDevices.value) return@ScannedDeviceReceiver
        Timber.i("FOUND NEW DEVICE: $device")
        _scannedDevices.update { it + device }
    }

    override fun getBluetoothState(): StateFlow<BtState> {
        Timber.d("REGISTERING BT STATE RECEIVER ... ")
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
        return _bluetoothState.asStateFlow()
    }

    @SuppressLint("MissingPermission")
    override fun getScannedDevices(): StateFlow<List<BtDevice>> {
        Timber.d("STARTING BT CLASSIC SCAN ... ")
        context.registerReceiver(
            scannedDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        if (context.hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            bluetoothAdapter?.startDiscovery()
        }
        return _scannedDevices.asStateFlow()
    }

    @SuppressLint("MissingPermission")
    override fun getPairedDevices(): StateFlow<List<BtDevice>> {
        Timber.d("FETCHING PAIRED DEVICES ... ")
        if (context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            _pairedDevices.update {
                bluetoothAdapter?.bondedDevices?.map{ it.simplify() } ?: emptyList()
            }
        }
        return _pairedDevices.asStateFlow()
    }

    @SuppressLint("MissingPermission")
    override fun stopDiscovery() {
        Timber.d("STOPPING DISCOVERY ... ")
        if (context.hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            bluetoothAdapter?.cancelDiscovery()
        }
        context.unregisterReceiver(scannedDeviceReceiver)
    }
}