package dev.atick.storage.preferences.data

interface PreferencesDatastore {
    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String
}