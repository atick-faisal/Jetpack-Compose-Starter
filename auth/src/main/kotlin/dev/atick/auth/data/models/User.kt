package dev.atick.auth.data.models

import android.net.Uri

data class User(
    val id: String,
    val name: String?,
    val profilePictureUri: Uri?
)
