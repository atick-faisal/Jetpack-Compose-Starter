package dev.atick.network.utils

import kotlinx.coroutines.flow.Flow

interface NetworkUtils {
    val currentState: Flow<NetworkState>
}