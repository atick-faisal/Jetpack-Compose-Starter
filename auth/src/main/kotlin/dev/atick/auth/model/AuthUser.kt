package dev.atick.auth.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class AuthUser(
    val id: String,
    val name: String,
    val profilePictureUri: Uri?
)

fun FirebaseUser.asAuthUser() = AuthUser(
    id = uid,
    name = displayName ?: "No Name",
    profilePictureUri = photoUrl,
)