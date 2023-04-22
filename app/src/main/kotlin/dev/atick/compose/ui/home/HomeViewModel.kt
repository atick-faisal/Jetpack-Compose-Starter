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
    private val jetpackRepository: HomeRepository,
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
