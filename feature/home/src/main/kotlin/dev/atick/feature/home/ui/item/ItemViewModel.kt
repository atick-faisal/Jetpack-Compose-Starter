package dev.atick.feature.home.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.extensions.asOneTimeEvent
import dev.atick.core.ui.utils.UiState
import dev.atick.core.ui.utils.updateState
import dev.atick.core.ui.utils.updateStateWith
import dev.atick.core.utils.OneTimeEvent
import dev.atick.data.models.home.Jetpack
import dev.atick.data.repository.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val jetpackId: String = TODO() // savedStateHandle.toRoute<Details>().jetpackId

    private val _itemUiState = MutableStateFlow(UiState(ItemScreenData()))
    val itemUiState = _itemUiState.asStateFlow()

    fun updateName(name: String) {
        _itemUiState.updateState { copy(jetpackName = name) }
    }

    fun updatePrice(priceString: String) {
        val price = priceString.toDoubleOrNull() ?: return
        _itemUiState.updateState { copy(jetpackPrice = price) }
    }

    fun updateOrInsertJetpack() {
        _itemUiState.updateStateWith(viewModelScope) {
            val jetpack = Jetpack(
                id = jetpackId,
                name = jetpackName,
                price = jetpackPrice,
            )
            if (jetpackId == null) homeRepository.insertJetpack(jetpack)
            else homeRepository.updateJetpack(jetpack)
            Result.success(copy(navigateBack = OneTimeEvent(true)))
        }
    }

    fun getJetpack() {
        jetpackId?.let {
            homeRepository.getJetpack(jetpackId)
                .onEach { jetpack ->
                    _itemUiState.updateState {
                        copy(
                            jetpackId = jetpack.id,
                            jetpackName = jetpack.name,
                            jetpackPrice = jetpack.price,
                        )
                    }
                }
                .catch { e -> UiState(ItemScreenData(), error = e.asOneTimeEvent()) }
                .launchIn(viewModelScope)
        }
    }
}

data class ItemScreenData(
    val jetpackId: String = "",
    val jetpackName: String = "",
    val jetpackPrice: Double = 0.0,
    val navigateBack: OneTimeEvent<Boolean> = OneTimeEvent(false),
)