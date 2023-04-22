package dev.atick.core.ui.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

inline fun ComponentActivity.resultLauncher(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {}
): ActivityResultLauncher<Intent> {
    val resultCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val success = (result.resultCode == Activity.RESULT_OK)
        if (success) onSuccess.invoke()
        else onFailure.invoke()
    }
    return resultCallback
}

inline fun ComponentActivity.permissionLauncher(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {}
): ActivityResultLauncher<Array<String>> {
    val resultCallback = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) onSuccess.invoke()
        else onFailure.invoke()
    }
    return resultCallback
}

inline fun ComponentActivity.checkForPermissions(
    permissions: List<String>,
    crossinline onSuccess: () -> Unit
) {
    if (isAllPermissionsGranted(permissions)) return
    val launcher = permissionLauncher(
        onSuccess = onSuccess,
        onFailure = {
            showToast("PLEASE ALLOW ALL PERMISSIONS")
            openPermissionSettings()
        }
    )
    launcher.launch(permissions.toTypedArray())
}

// ... Open App Settings
// ... https://stackoverflow.com/a/37093460/12737399
fun ComponentActivity.openPermissionSettings() {
    val intent = Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}