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
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.data.home.UiPost
import dev.atick.compose.navigation.details.postIdArg
import dev.atick.compose.repository.home.PostsRepository
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    postsRepository: PostsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val postId = checkNotNull(savedStateHandle.get<Int>(postIdArg))

    private val _detailsUiState: MutableStateFlow<UiState<UiPost?>> =
        MutableStateFlow(UiState.Loading(null))
    val detailsUiState = _detailsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = postsRepository.getPost(postId)
            if (result.isSuccess) {
                _detailsUiState.update { UiState.Success(result.getOrNull()) }
            } else {
                _detailsUiState.update { UiState.Error(null, result.exceptionOrNull()) }
            }
        }
    }
}
