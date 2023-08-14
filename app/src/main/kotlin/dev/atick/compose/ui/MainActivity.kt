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
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
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
            val darkTheme = isSystemInDarkTheme()

            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            JetpackTheme(
                darkTheme = darkTheme,
                androidTheme = false, // TODO: Implementation required
                disableDynamicTheming = false,
            ) {
                JetpackApp(
                    networkUtils = networkUtils,
                    windowSizeClass = calculateWindowSizeClass(this),
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

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
