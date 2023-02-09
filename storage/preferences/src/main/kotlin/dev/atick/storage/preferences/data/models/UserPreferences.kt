package dev.atick.storage.preferences.data.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserPreferences(
    @SerialName("user_id")
    val userId: String = "-1"
)
