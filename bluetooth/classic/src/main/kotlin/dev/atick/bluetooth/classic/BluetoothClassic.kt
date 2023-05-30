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

package dev.atick.bluetooth.classic

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.common.data.BluetoothDataSource
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.bluetooth.common.models.BtMessage
import dev.atick.bluetooth.common.models.BtState
import dev.atick.bluetooth.common.models.simplify
import dev.atick.bluetooth.common.receiver.BluetoothStateReceiver
import dev.atick.bluetooth.common.receiver.DeviceStateReceiver
import dev.atick.bluetooth.common.receiver.ScannedDeviceReceiver
import dev.atick.bluetooth.common.utils.BluetoothUtils
import dev.atick.core.di.IoDispatcher
import dev.atick.core.extensions.hasPermission
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothClassic @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BluetoothUtils, BluetoothManager, BluetoothDataSource {

    companion object {
        const val BT_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    private var bluetoothSocket: BluetoothSocket? = null

    private val _bluetoothState = MutableStateFlow(
        if (bluetoothAdapter?.isEnabled == true) {
            BtState.ENABLED
        } else {
            BtState.DISABLED
        },
    )

    private val _scannedDevices = MutableStateFlow(emptyList<BtDevice>())
    private val _pairedDevices = MutableStateFlow(emptyList<BtDevice>())
    private val _deviceState = MutableStateFlow<BtDevice?>(null)
    private val _bluetoothMessage = MutableStateFlow<BtMessage?>(null)
    private var connectedDeviceAddress: String? = null

    private val bluetoothStateReceiver = BluetoothStateReceiver { state ->
        Timber.i("BT STATE UPDATE: $state")
        _bluetoothState.update { state }
    }

    private val scannedDeviceReceiver = ScannedDeviceReceiver { device ->
        if (device in _scannedDevices.value) return@ScannedDeviceReceiver
        Timber.i("FOUND NEW DEVICE: $device")
        _scannedDevices.update { it + device }
    }

    private val deviceStateReceiver = DeviceStateReceiver { device ->
        if (connectedDeviceAddress != device.address) return@DeviceStateReceiver
        _deviceState.update { device }
    }

    override fun getBluetoothState(): StateFlow<BtState> {
        Timber.d("REGISTERING BT STATE RECEIVER ... ")
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
        )
        return _bluetoothState.asStateFlow()
    }

    @SuppressLint("MissingPermission")
    override fun getScannedDevices(): StateFlow<List<BtDevice>> {
        Timber.d("STARTING BT CLASSIC SCAN ... ")
        clearScannedDevices()
        context.registerReceiver(
            scannedDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND),
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
                bluetoothAdapter?.bondedDevices?.map { it.simplify() } ?: emptyList()
            }
        }
        return _pairedDevices.asStateFlow()
    }

    @SuppressLint("MissingPermission")
    override fun stopDiscovery() {
        Timber.d("STOPPING DISCOVERY ... ")
        if (context.hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            try {
                bluetoothAdapter?.cancelDiscovery()
                context.unregisterReceiver(scannedDeviceReceiver)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun connect(address: String): Result<Unit> {
        if (!context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return Result.failure(SecurityException("Missing Permission"))
        }
        if (bluetoothSocket?.isConnected == true) {
            return Result.failure(IllegalStateException("Please Close Existing Connection"))
        }
        Timber.d("INITIATING CONNECTION ... ")
        bluetoothSocket = bluetoothAdapter?.getRemoteDevice(address)
            ?.createInsecureRfcommSocketToServiceRecord(UUID.fromString(BT_UUID))
        return try {
            withContext(ioDispatcher) {
                bluetoothSocket?.run { connect() }
                connectedDeviceAddress = address
                listenForIncomingBluetoothMessages()
                Result.success(Unit)
            }
        } catch (e: IOException) {
            Timber.e(e)
            Result.failure(e)
        }
    }

    override fun getConnectedDeviceState(): StateFlow<BtDevice?> {
        Timber.d("FETCHING DEVICE STATE ... ")
        context.registerReceiver(
            deviceStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            },
        )
        return _deviceState.asStateFlow()
    }

    override suspend fun closeConnection(): Result<Unit> {
        Timber.d("CLOSING CONNECTION ... ")
        return try {
            withContext(ioDispatcher) {
                bluetoothSocket?.close()
                while (_deviceState.value?.connected == true) {
                    delay(1000L)
                }
                cleanup()
                Result.success(Unit)
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override fun getBluetoothDataStream(): StateFlow<BtMessage?> {
        return _bluetoothMessage.asStateFlow()
    }

    override suspend fun sendDataToBluetoothDevice(data: String): Result<Unit> {
        Timber.d("SENDING : $data")
        return try {
            withContext(ioDispatcher) {
                bluetoothSocket?.outputStream?.write(data.toByteArray())
                Result.success(Unit)
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    private suspend fun listenForIncomingBluetoothMessages() {
        Timber.d("LISTENING FOR BLUETOOTH MESSAGES ... ")
        Timber.d("SOCKET: $bluetoothSocket")
        withContext(ioDispatcher) {
            bluetoothSocket?.run {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                while (isConnected) {
                    try {
                        if (bufferedReader.ready()) {
                            val message = bufferedReader.readLine()
                            _bluetoothMessage.update { BtMessage(message = message) }
                            Timber.i("RECEIVED MESSAGE: $message")
                        }
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun clearScannedDevices() {
        _scannedDevices.update { emptyList() }
    }

    private fun cleanup() {
        Timber.d("CLEANING UP ... ")
        // bluetoothSocket = null
        connectedDeviceAddress = null
        context.unregisterReceiver(bluetoothStateReceiver)
        context.unregisterReceiver(deviceStateReceiver)
        clearScannedDevices()
    }
}
