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

package dev.atick.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Provides the activity from Context (https://stackoverflow.com/a/68423182/12737399)
 *
 * @return The activity associated with the context, or `null` if the context is not an activity.
 */
fun Context.getActivity(): ComponentActivity? {
    return when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }
}

/**
 * Displays a short toast message.
 *
 * @param message The message to be displayed in the toast.
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Checks if the app has a given permission.
 *
 * @param permission The permission to check.
 * @return `true` if the permission is granted, `false` otherwise.
 */
fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks if all the given permissions are granted.
 *
 * @param permissions List of permissions to check.
 * @return `true` if all permissions are granted, `false` otherwise.
 */
fun Context.isAllPermissionsGranted(permissions: List<String>): Boolean {
    return permissions.all { hasPermission(it) }
}

/**
 * Shows a notification using the specified notification ID and notification object.
 *
 * @param notificationId The ID of the notification.
 * @param notification The notification object to be shown.
 */
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

/**
 * Cancels a previously shown notification.
 *
 * @param notificationId The ID of the notification to be canceled.
 */
fun Context.cancelNotification(notificationId: Int) {
    with(NotificationManagerCompat.from(this)) {
        cancel(notificationId)
    }
}

/**
 * Retrieves a temporary file URI for the specified app ID.
 *
 * @param appId The ID of the app.
 * @return The URI of the temporary file.
 * @throws IllegalAccessException if unable to create or retrieve the temporary file.
 */
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

/**
 * Retrieves a File object from the given content URI.
 *
 * @param contentUri The content URI of the file.
 * @return The File object representing the content URI, or `null` if an error occurred.
 */
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

/**
 * Retrieves the file extension from the given content URI.
 *
 * @param context The context.
 * @param uri The content URI of the file.
 * @return The file extension, or `null` if the extension could not be determined.
 */
private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

/**
 * Copies the data from the input stream to the output stream.
 *
 * @param source The input stream to read from.
 * @param target The output stream to write to.
 * @throws IOException if an I/O error occurs during the copy operation.
 */
@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}
