package dev.atick.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.repository.user.UserDataRepository
import dev.atick.core.extensions.stateInDelayed
import dev.atick.core.ui.utils.UiState
import dev.atick.storage.preferences.model.UserData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<UiState<UserData>> = userDataRepository.userData
        .catch { throwable -> UiState.Error(UserData(), throwable) }
        .map { userData -> UiState.Success(userData) }
        .stateInDelayed(UiState.Loading(UserData()), viewModelScope)

}