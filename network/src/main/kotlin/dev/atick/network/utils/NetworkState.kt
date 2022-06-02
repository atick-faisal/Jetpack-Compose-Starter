package dev.atick.network.utils

import androidx.annotation.StringRes
import dev.atick.network.R

enum class NetworkState(@StringRes val description: Int) {
    CONNECTED(R.string.network_connected),
    LOSING(R.string.network_losing),
    LOST(R.string.network_lost),
    UNAVAILABLE(R.string.network_not_available)
}