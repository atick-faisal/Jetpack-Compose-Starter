package dev.atick.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.extensions.asOneTimeEvent
import dev.atick.core.ui.utils.UiState
import dev.atick.core.ui.utils.updateWith
import dev.atick.data.models.Profile
import dev.atick.data.repository.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(UiState(Profile()))
    val profileUiState: StateFlow<UiState<Profile>>
        get() = _profileUiState.asStateFlow()

    fun updateProfileData() {
        profileRepository.getProfile()
            .map { profileScreenData -> UiState(profileScreenData) }
            .onEach { data -> _profileUiState.update { data } }
            .catch { e -> UiState(Profile(), error = e.asOneTimeEvent()) }
            .launchIn(viewModelScope)
    }

    fun signOut() {
        _profileUiState.updateWith(viewModelScope) { profileRepository.signOut() }
    }
}