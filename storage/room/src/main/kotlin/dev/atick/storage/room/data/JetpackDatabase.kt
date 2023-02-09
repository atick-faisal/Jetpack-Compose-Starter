package dev.atick.storage.room.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.atick.storage.room.data.models.Item

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Item::class
    ]
)
abstract class JetpackDatabase : RoomDatabase() {
    abstract fun getJetpackDao(): JetpackDao
}