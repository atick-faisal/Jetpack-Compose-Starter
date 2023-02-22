package dev.atick.compose.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.repository.home.HomeRepository
import dev.atick.compose.ui.home.state.HomeUiState
import dev.atick.compose.ui.home.state.setLoading
import dev.atick.compose.ui.home.state.setToastMessage
import dev.atick.core.ui.base.BaseViewModel
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
    private val jetpackRepository: HomeRepository
) : BaseViewModel<HomeUiState>() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private var getItemJob: Job? = null

    init {
        viewModelScope.launch {
            val result = jetpackRepository.getUserId()
            if (result.isSuccess) {
                val userId = result.getOrNull().toString()
                _homeUiState.setToastMessage(userId)
            }
        }
    }

    fun getItem() {
        if (getItemJob != null) return
        getItemJob = viewModelScope.launch {
            _homeUiState.setLoading(true)
            val result = jetpackRepository.getItem(Random.nextInt(-100, 100))
            if (result.isSuccess) {
                val item = result.getOrNull()
                item?.let {
                    _homeUiState.update { it.copy(item = item) }
                    jetpackRepository.saveItem(item)
                }
            } else {
                _homeUiState.setToastMessage("Error Loading Item")
            }
            _homeUiState.setLoading(false)
            getItemJob = null
        }
    }

    fun clearError() {
        _homeUiState.setToastMessage(null)
    }
}