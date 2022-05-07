package dev.atick.core.utils.extensions

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.atick.core.BuildConfig

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.debugMessage(error: String) {
    if (BuildConfig.DEBUG) {
        this.showToast(error)
    }
}

fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
        PackageManager.PERMISSION_GRANTED
}

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveText: String = "OK",
    negativeText: String = "CANCEL",
    onApprove: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(positiveText) { _, _ ->
        onApprove.invoke()
    }

    builder.setNegativeButton(negativeText) { _, _ ->
        onCancel.invoke()
    }

    builder.setCancelable(false)
    builder.show()
}

fun Context.showNotification(
    notificationId: Int,
    notification: Notification
) {
    with(NotificationManagerCompat.from(this)) {
        notify(notificationId, notification)
    }
}