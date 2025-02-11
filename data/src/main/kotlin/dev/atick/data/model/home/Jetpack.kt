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

package dev.atick.data.model.home

import dev.atick.core.extensions.asFormattedDateTime
import dev.atick.core.room.model.JetpackEntity
import dev.atick.firebase.firestore.model.FirebaseJetpack
import java.util.UUID

/**
 * Data class representing a Jetpack.
 *
 * @param id The unique identifier of the Jetpack.
 * @param name The name of the Jetpack.
 * @param price The price of the Jetpack.
 * @param lastUpdated The last updated timestamp of the Jetpack.
 * @param lastSynced The last synced timestamp of the Jetpack.
 * @param needsSync The sync status of the Jetpack.
 * @param formattedDate The formatted date of the Jetpack.
 */
data class Jetpack(
    val id: String = UUID.randomUUID().toString(),
    val name: String = String(),
    val price: Double = 0.0,
    val lastUpdated: Long = System.currentTimeMillis(),
    val lastSynced: Long = 0L,
    val needsSync: Boolean = true,
    val formattedDate: String = lastUpdated.asFormattedDateTime(),
)

/**
 * Extension function to map a [JetpackEntity] to a [Jetpack].
 *
 * @return The mapped [Jetpack].
 */
fun JetpackEntity.toJetpack(): Jetpack {
    return Jetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        needsSync = needsSync,
        formattedDate = lastUpdated.asFormattedDateTime(),
    )
}

/**
 * Extension function to map a list of [JetpackEntity] to a list of [Jetpack].
 *
 * @return The mapped list of [Jetpack].
 */
fun List<JetpackEntity>.mapToJetpacks(): List<Jetpack> {
    return map(JetpackEntity::toJetpack)
}

/**
 * Extension function to map a [Jetpack] to a [JetpackEntity].
 *
 * @return The mapped [JetpackEntity].
 */
fun Jetpack.toJetpackEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

/**
 * Extension function to map a [Jetpack] to a [FirebaseJetpack].
 *
 * @return The mapped [FirebaseJetpack].
 */
fun Jetpack.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

/**
 * Extension function to map a [JetpackEntity] to a [FirebaseJetpack].
 *
 * @return The mapped [FirebaseJetpack].
 */
fun JetpackEntity.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        userId = userId,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        deleted = deleted,
    )
}

/**
 * Extension function to map a [FirebaseJetpack] to a [JetpackEntity].
 *
 * @return The mapped [JetpackEntity].
 */
fun FirebaseJetpack.toJetpackEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        userId = userId,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        deleted = deleted,
    )
}
