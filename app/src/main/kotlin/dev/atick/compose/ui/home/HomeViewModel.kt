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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.data.home.HomeScreenData
import dev.atick.compose.repository.home.PostsRepository
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the home screen.
 *
 * @param postsRepository The repository for accessing home screen data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
) : ViewModel() {
    private val _homeUiState: MutableStateFlow<UiState<HomeScreenData>> =
        MutableStateFlow(UiState.Loading(HomeScreenData()))
    val homeUiState = _homeUiState.asStateFlow()

    init {
        postsRepository.getCachedPosts()
            .map(::HomeScreenData)
            .onEach { homeScreenData ->
                _homeUiState.update {
                    UiState.Success(homeScreenData)
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            _homeUiState.update { UiState.Loading(HomeScreenData()) }
            val result = postsRepository.synchronizePosts()
            if (result.isFailure) {
                _homeUiState.update {
                    UiState.Error(
                        homeUiState.value.data,
                        result.exceptionOrNull(),
                    )
                }
            }
        }
    }
}
