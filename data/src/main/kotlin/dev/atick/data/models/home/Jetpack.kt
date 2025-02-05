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

package dev.atick.data.models.home

import dev.atick.core.extensions.asFormattedDateTime
import dev.atick.core.room.models.JetpackEntity
import dev.atick.firebase.firestore.models.FirebaseJetpack
import java.util.UUID

data class Jetpack(
    val id: String = UUID.randomUUID().toString(),
    val name: String = String(),
    val price: Double = 0.0,
    val lastUpdated: Long = System.currentTimeMillis(),
    val lastSynced: Long = 0L,
    val needsSync: Boolean = true,
    val formattedDate: String = lastUpdated.asFormattedDateTime(),
)

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

fun List<JetpackEntity>.mapToJetpacks(): List<Jetpack> {
    return map(JetpackEntity::toJetpack)
}

fun Jetpack.toJetpackEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

fun Jetpack.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

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
