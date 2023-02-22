package dev.atick.compose.ui.home.state

import dev.atick.compose.data.home.Item
import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    override val loading: Boolean = false,
    override val toastMessage: UiText? = null,
    val item: Item? = null
) : BaseUiState()

fun MutableStateFlow<HomeUiState>.setLoading(loading: Boolean) {
    update { it.copy(loading = loading) }
}

fun MutableStateFlow<HomeUiState>.setToastMessage(message: String?) {
    if (message == null) update { it.copy(toastMessage = null) }
    else update { it.copy(toastMessage = UiText.DynamicString(message)) }
}
