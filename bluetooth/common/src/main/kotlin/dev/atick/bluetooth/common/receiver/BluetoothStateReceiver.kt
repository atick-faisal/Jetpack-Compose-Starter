package dev.atick.bluetooth.common.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.atick.bluetooth.common.models.BtState

class BluetoothStateReceiver(
    private val onBtStateChange: (BtState) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_ON -> onBtStateChange(BtState.ENABLED)
            BluetoothAdapter.STATE_OFF -> onBtStateChange(BtState.DISABLED)
        }
    }
}