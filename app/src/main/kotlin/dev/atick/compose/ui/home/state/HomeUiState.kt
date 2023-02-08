package dev.atick.compose.ui.home.state

import dev.atick.compose.data.home.Item
import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText

data class HomeUiState(
    val loading: Boolean = false,
    val error: UiText? = null,
    val item: Item? = null
) : BaseUiState()
