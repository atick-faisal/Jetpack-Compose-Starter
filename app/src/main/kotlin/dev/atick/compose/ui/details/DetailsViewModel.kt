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