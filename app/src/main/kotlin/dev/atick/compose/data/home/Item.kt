package dev.atick.compose.data.home

import dev.atick.storage.room.data.models.Item

data class Item(
    val id: Int,
    val title: String
) {
    fun toRoomItem(): Item {
        return Item(name = title)
    }
}
