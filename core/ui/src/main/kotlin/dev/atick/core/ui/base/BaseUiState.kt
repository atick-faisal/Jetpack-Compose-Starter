package dev.atick.core.ui.base

import dev.atick.core.ui.utils.UiText

abstract class BaseUiState {
    abstract val loading: Boolean
    abstract val toastMessage: UiText?
}