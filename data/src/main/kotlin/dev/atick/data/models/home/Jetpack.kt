package dev.atick.data.models.home

import dev.atick.core.extensions.asFormattedDateTime
import dev.atick.core.room.models.JetpackEntity
import dev.atick.core.room.models.SyncAction
import dev.atick.firebase.firestore.models.FirebaseJetpack
import java.util.UUID

data class Jetpack(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
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
        lastUpdated = System.currentTimeMillis(),
        lastSynced = lastSynced,
        needsSync = true,
    )
}

fun Jetpack.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = System.currentTimeMillis(),
    )
}

fun JetpackEntity.toFirebaseJetpack(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
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
        lastUpdated = lastUpdated,
        lastSynced = System.currentTimeMillis(),
        needsSync = false,
        deleted = deleted,
        syncAction = SyncAction.NONE,
    )
}