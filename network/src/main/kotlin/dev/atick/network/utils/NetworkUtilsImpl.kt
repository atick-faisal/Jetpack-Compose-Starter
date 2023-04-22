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

package dev.atick.network.utils

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.orhanobut.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkUtilsImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : NetworkUtils {
    override val currentState: Flow<NetworkState>
        get() = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(NetworkState.CONNECTED)
                    Logger.i("NETWORK CONNECTED")
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    trySend(NetworkState.LOSING)
                    Logger.i("LOSING NETWORK CONNECTION ... ")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkState.LOST)
                    Logger.i("NETWORK CONNECTION LOST")
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(NetworkState.UNAVAILABLE)
                    Logger.i("NETWORK UNAVAILABLE")
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities,
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    Logger.i("NETWORK TYPE CHANGED")
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties,
                ) {
                    super.onLinkPropertiesChanged(network, linkProperties)
                    Logger.i("LINK PROPERTIES CHANGED")
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    Logger.i("BLOCKED STATUS CHANGED")
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
}
