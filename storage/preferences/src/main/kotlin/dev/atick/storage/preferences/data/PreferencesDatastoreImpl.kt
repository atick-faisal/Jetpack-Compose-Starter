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

package dev.atick.storage.preferences.data

import androidx.datastore.core.DataStore
import dev.atick.storage.preferences.data.models.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesDatastoreImpl @Inject constructor(
    private val datastore: DataStore<UserPreferences>,
) : PreferencesDatastore {
    override suspend fun saveUserId(userId: String) {
        datastore.updateData { it.copy(userId = userId) }
    }

    override suspend fun getUserId(): String {
        return datastore.data.first().userId
    }
}
