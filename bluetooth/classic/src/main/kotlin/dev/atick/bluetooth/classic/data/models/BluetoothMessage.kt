package dev.atick.bluetooth.classic.data.models

import java.util.*

data class BluetoothMessage(
    val timestamp: Long = Date().time,
    val message: String? = null
)
