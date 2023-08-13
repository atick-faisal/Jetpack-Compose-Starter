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

package dev.atick.compose.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.common.models.BtState
import dev.atick.bluetooth.common.utils.BluetoothUtils
import dev.atick.compose.R
import dev.atick.core.extensions.isAllPermissionsGranted
import dev.atick.core.ui.extensions.checkForPermissions
import dev.atick.core.ui.extensions.collectWithLifecycle
import dev.atick.core.ui.extensions.resultLauncher
import dev.atick.core.ui.theme.JetpackTheme
import dev.atick.network.utils.NetworkUtils
import javax.inject.Inject

/**
 * Main activity for the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissions = mutableListOf<String>()
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var bluetoothUtils: BluetoothUtils

    @Inject
    lateinit var networkUtils: NetworkUtils

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            JetpackTheme {
                JetpackApp(
                    windowSizeClass = calculateWindowSizeClass(activity = this),
                    networkUtils = networkUtils,
                )
            }
        }

        // Configure required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        // Check for permissions and launch Bluetooth enable request
        checkForPermissions(permissions) {
            btLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
            )
        }

        // Configure Bluetooth state monitoring and enable request
        btLauncher = resultLauncher(onFailure = { finishAffinity() })
        collectWithLifecycle(bluetoothUtils.getBluetoothState()) { state ->
            if (state == BtState.DISABLED && isAllPermissionsGranted(permissions)) {
                btLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                )
            }
        }
    }
}
