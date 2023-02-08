package dev.atick.compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.repository.home.JetpackRepository
import dev.atick.compose.ui.home.state.HomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jetpackRepository: JetpackRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private var getItemJob: Job? = null

    fun getItem() {
        if (getItemJob != null) return
        getItemJob = viewModelScope.launch {
            _homeUiState.update { it.copy(loading = true) }
            val result = jetpackRepository.getItem(Random(42).nextInt())
            if (result.isSuccess) {
                _homeUiState.update { it.copy(item = result.getOrNull(), loading = false) }
            } else {
                _homeUiState.update { it.copy(error = "Error Loading Item", loading = false) }
            }
            getItemJob = null
        }
    }

    fun clearError() {
        _homeUiState.update { it.copy(error = null) }
    }
}