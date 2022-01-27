package dev.atick.core.utils.extensions

import android.content.Context
import android.widget.Toast
import dev.atick.core.BuildConfig

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.debugMessage(error: String) {
    if (BuildConfig.DEBUG) {
        this.showToast(error)
    }
}