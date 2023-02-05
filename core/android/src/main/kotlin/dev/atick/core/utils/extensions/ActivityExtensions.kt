package dev.atick.core.utils.extensions

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

fun ComponentActivity.resultLauncher(
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {}

): ActivityResultLauncher<Intent> {
    val resultCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSuccess.invoke()
        } else {
            onFailure.invoke()
        }
    }
    return resultCallback
}

fun ComponentActivity.permissionLauncher(
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {}
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