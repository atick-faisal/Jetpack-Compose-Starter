package dev.atick.bluetooth.classic

//import android.bluetooth.BluetoothAdapter
//import android.content.Context
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dev.atick.bluetooth.classic.receiver.FoundDeviceReceiver
//import dev.atick.bluetooth.classic.utils.BluetoothClassicDevice
//import dev.atick.bluetooth.classic.utils.BluetoothState
//import dev.atick.bluetooth.classic.utils.BluetoothUtils
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//class BluetoothHelper(
//    private val bluetoothAdapter: BluetoothAdapter?,
//    @ApplicationContext private val context: Context
//) : BluetoothUtils {
//
//    private val _bluetoothState = MutableStateFlow(
//        if (bluetoothAdapter?.isEnabled == true) BluetoothState.ENABLED
//        else BluetoothState.DISABLED
//    )
//    private val _scannedDevices = MutableStateFlow(emptyList<BluetoothClassicDevice>())
//
//    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
//        if (device in _scannedDevices.value) return@FoundDeviceReceiver
//        // _scannedDevices.update { it + BluetoothClassicDevice(device) }
//    }
//
//    override val bluetoothState: StateFlow<BluetoothState>
//        get() = _bluetoothState.asStateFlow()
//
//    override fun getScannedDevices(): StateFlow<List<BluetoothClassicDevice>> {
//        return _scannedDevices.asStateFlow()
//    }
//
//
//}