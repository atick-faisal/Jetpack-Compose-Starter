package dev.atick.storage.preferences.data

import androidx.datastore.core.DataStore
import dev.atick.storage.preferences.data.models.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesDatastoreImpl @Inject constructor(
    private val datastore: DataStore<UserPreferences>
) : PreferencesDatastore {
    override suspend fun saveUserId(userId: String) {
        datastore.updateData { it.copy(userId = userId) }
    }

    override suspend fun getUserId(): String {
        return datastore.data.first().userId
    }
}