package dev.atick.compose.ui.home.state

import dev.atick.compose.data.home.Item

data class HomeUiState(
    val item: Item? = null,
    val loading: Boolean = false,
    val error: String? = null
)
