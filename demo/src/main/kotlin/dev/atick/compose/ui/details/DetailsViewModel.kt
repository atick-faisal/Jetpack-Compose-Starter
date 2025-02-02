/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.compose.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.data.details.DetailsScreenData
import dev.atick.compose.data.home.Jetpack
import dev.atick.compose.navigation.details.Details
import dev.atick.compose.repository.home.HomeRepository
import dev.atick.core.extensions.asOneTimeEvent
import dev.atick.core.ui.utils.UiState
import dev.atick.core.ui.utils.updateState
import dev.atick.core.ui.utils.updateStateWith
import dev.atick.core.utils.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val jetpackId = savedStateHandle.toRoute<Details>().jetpackId

    private val _detailsUiState = MutableStateFlow(UiState(DetailsScreenData()))
    val detailsUiState = _detailsUiState.asStateFlow()

    fun updateName(name: String) {
        _detailsUiState.updateState { copy(name = name) }
    }

    fun updatePrice(priceString: String) {
        val price = priceString.toDoubleOrNull() ?: return
        _detailsUiState.updateState { copy(price = price) }
    }

    fun updateOrInsertJetpack() {
        if (jetpackId == null) {
            _detailsUiState.updateStateWith(viewModelScope) {
                homeRepository.insertJetpack(
                    Jetpack(
                        id = detailsUiState.value.data.id,
                        name = detailsUiState.value.data.name,
                        price = detailsUiState.value.data.price,
                    ),
                )
                Result.success(detailsUiState.value.data.copy(navigateBack = OneTimeEvent(true)))
            }
        } else {
            _detailsUiState.updateStateWith(viewModelScope) {
                homeRepository.updateJetpack(
                    Jetpack(
                        id = detailsUiState.value.data.id,
                        name = detailsUiState.value.data.name,
                        price = detailsUiState.value.data.price,
                    ),
                )
                Result.success(detailsUiState.value.data.copy(navigateBack = OneTimeEvent(true)))
            }
        }
    }

    fun getJetpack() {
        jetpackId?.let {
            homeRepository.getJetpack(jetpackId)
                .onEach { jetpack ->
                    _detailsUiState.updateState {
                        copy(
                            id = jetpack.id,
                            name = jetpack.name,
                            price = jetpack.price,
                        )
                    }
                }
                .catch { e -> UiState(DetailsScreenData(), error = e.asOneTimeEvent()) }
                .launchIn(viewModelScope)
        }
    }
}
