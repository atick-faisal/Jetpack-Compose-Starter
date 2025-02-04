package dev.atick.feature.home.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.extensions.asOneTimeEvent
import dev.atick.core.ui.utils.UiState
import dev.atick.data.models.home.Jetpack
import dev.atick.data.repository.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(UiState(HomeScreenData()))
    val homeUiState = _homeUiState.asStateFlow()

    fun getJetpacks() {
        homeRepository.getJetpacks()
            .map(::HomeScreenData)
            .onEach { homeScreenData -> _homeUiState.update { UiState(homeScreenData) } }
            .catch { e -> _homeUiState.update { it.copy(error = e.asOneTimeEvent()) } }
            .launchIn(viewModelScope)
    }
}

@Immutable
data class HomeScreenData(
    val jetpacks: List<Jetpack> = emptyList(),
)