package dev.atick.core.ui.extensions

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

fun Context.isAllPermissionsGranted(permissions: List<String>): Boolean {
    return permissions.map { permission ->
        this.hasPermission(permission)
    }.reduce { acc, b -> acc && b }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Context.showNotification(
    notificationId: Int,
    notification: Notification
) {
    with(NotificationManagerCompat.from(this)) {
        notify(notificationId, notification)
    }
}