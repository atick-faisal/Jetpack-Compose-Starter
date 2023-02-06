package dev.atick.compose.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.ui.home.data.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    fun click() {
        if (homeUiState.value.nClicks < 3)
            _homeUiState.update { it.copy(nClicks = it.nClicks + 1) }
        else
            _homeUiState.update { it.copy(error = "STOP!") }
    }

    fun clearError() {
        _homeUiState.update { it.copy(error = null) }
    }
}