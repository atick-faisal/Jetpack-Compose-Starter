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

package dev.atick.core.ui.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dev.atick.core.extensions.isAllPermissionsGranted
import dev.atick.core.extensions.showToast

/**
 * Launch an activity for result.
 *
 * @param onSuccess Callback when the result is successful.
 * @param onFailure Callback when the result is failed.
 */
inline fun ComponentActivity.resultLauncher(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {},
): ActivityResultLauncher<Intent> {
    val resultCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val success = (result.resultCode == Activity.RESULT_OK)
        if (success) {
            onSuccess.invoke()
        } else {
            onFailure.invoke()
        }
    }
    return resultCallback
}

/**
 * Launch an activity for permission.
 *
 * @param onSuccess Callback when the result is successful.
 * @param onFailure Callback when the result is failed.
 */
inline fun ComponentActivity.permissionLauncher(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {},
): ActivityResultLauncher<Array<String>> {
    val resultCallback = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            onSuccess.invoke()
        } else {
            onFailure.invoke()
        }
    }
    return resultCallback
}

/**
 * Check for permissions.
 *
 * @param permissions List of permissions to be checked.
 * @param onSuccess Callback when the result is successful.
 */
inline fun ComponentActivity.checkForPermissions(
    permissions: List<String>,
    crossinline onSuccess: () -> Unit,
) {
    if (isAllPermissionsGranted(permissions)) return
    val launcher = permissionLauncher(
        onSuccess = onSuccess,
        onFailure = {
            showToast("PLEASE ALLOW ALL PERMISSIONS")
            openPermissionSettings()
        },
    )
    launcher.launch(permissions.toTypedArray())
}

// ... Open App Settings
// ... https://stackoverflow.com/a/37093460/12737399

/**
 * Open app settings.
 */
fun ComponentActivity.openPermissionSettings() {
    val intent = Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName"),
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}
