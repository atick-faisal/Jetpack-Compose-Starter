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

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

fun Context.isAllPermissionsGranted(permissions: List<String>): Boolean {
    return permissions.all { hasPermission(it) }
}

@SuppressLint("MissingPermission")
fun Context.showNotification(
    notificationId: Int,
    notification: Notification,
) {
    if (hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }
    }
}

fun Context.cancelNotification(notificationId: Int) {
    with(NotificationManagerCompat.from(this)) {
        cancel(notificationId)
    }
}

// ... https://medium.com/codex/how-to-implement-the-activity-result-api-takepicture-contract-with-uri-return-type-7c93881f5b0f
@Throws(IllegalAccessException::class)
fun Context.getTmpFileUri(appId: String): Uri {
    val tmpFile = File.createTempFile(
        "tmp_image_file",
        ".png",
        cacheDir,
    ).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        applicationContext,
        "$appId.provider",
        tmpFile,
    )
}

// ... https://stackoverflow.com/a/64488260/12737399
fun Context.getFileFromContentUri(contentUri: Uri): File? {
    return try {
        val fileExtension = getFileExtension(this, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
        val tempFile = File(cacheDir, fileName)
        tempFile.createNewFile()
        val oStream = FileOutputStream(tempFile)
        val inputStream = contentResolver.openInputStream(contentUri)
        inputStream?.let { copy(inputStream, oStream) }
        oStream.flush()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}
