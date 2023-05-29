package dev.atick.bluetooth.common.models

import java.util.Date

data class BtMessage(
    val timestamp: Long = Date().time,
    val message: CharSequence
)
