/*
 * Copyright 2025 Atick Faisal
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

package dev.atick.firebase.firestore.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a Jetpack stored in Firebase.
 *
 * @property id Unique identifier of the Jetpack.
 * @property name Jetpack's name.
 * @property price Jetpack's price.
 * @property userId User's unique identifier. Defaults to an empty string.
 * @property lastUpdated Timestamp (milliseconds) of last modification. Defaults to 0L.
 * @property lastSynced Timestamp (milliseconds) of last sync. Defaults to 0L.
 * @property deleted Flag indicating if the Jetpack is marked for deletion (soft delete). Defaults to false.
 */
@Serializable
data class FirebaseJetpack(
    // Every property has to be initialized.
    // https://stackoverflow.com/a/67298049/12737399.
    val id: String = UUID.randomUUID().toString(),
    val name: String = String(),
    val price: Double = 0.0,
    val userId: String = String(),
    val lastUpdated: Long = 0L,
    val lastSynced: Long = 0L,
    val deleted: Boolean = false,
)
