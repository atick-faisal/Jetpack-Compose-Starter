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

package dev.atick.compose.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.data.settings.UserEditableSettings
import dev.atick.compose.repository.user.UserDataRepository
import dev.atick.core.ui.utils.OneTimeEvent
import dev.atick.core.ui.utils.UiState
import dev.atick.core.ui.utils.updateWith
import dev.atick.storage.preferences.models.DarkThemeConfig
import dev.atick.storage.preferences.models.ThemeBrand
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
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _settingsUiState = MutableStateFlow(UiState(UserEditableSettings()))
    val settingsUiState: StateFlow<UiState<UserEditableSettings>>
        get() = _settingsUiState.asStateFlow()

//    init {
//        userDataRepository.userData
//            .map { userData ->
//                UiState(
//                    UserEditableSettings(
//                        brand = userData.themeBrand,
//                        useDynamicColor = userData.useDynamicColor,
//                        darkThemeConfig = userData.darkThemeConfig,
//                    ),
//                )
//            }
//            .onEach { _settingsUiState.update { it } }
//            .catch { e -> UiState(UserEditableSettings(), error = OneTimeEvent(e)) }
//            .launchIn(viewModelScope)
//    }

    fun updateUserData() {
        userDataRepository.userData
            .map { userData ->
                UiState(
                    UserEditableSettings(
                        brand = userData.themeBrand,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .onEach { data -> _settingsUiState.update { data } }
            .catch { e -> UiState(UserEditableSettings(), error = OneTimeEvent(e)) }
            .launchIn(viewModelScope)
    }

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        _settingsUiState.updateWith(viewModelScope) { userDataRepository.setThemeBrand(themeBrand) }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        _settingsUiState.updateWith(viewModelScope) {
            userDataRepository.setDarkThemeConfig(
                darkThemeConfig,
            )
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        _settingsUiState.updateWith(viewModelScope) {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
}
