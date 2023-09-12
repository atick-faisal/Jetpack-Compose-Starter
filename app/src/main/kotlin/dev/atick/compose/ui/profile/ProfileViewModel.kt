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

package dev.atick.compose.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.compose.repository.profile.ProfileDataRepository
import dev.atick.core.extensions.stateInDelayed
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDataRepository: ProfileDataRepository,
) : ViewModel() {
    val profileUiState: StateFlow<UiState<ProfileScreenData>> =
        profileDataRepository.profileScreenData
            .map { UiState.Success(it) }
            .catch { UiState.Error(ProfileScreenData(), it) }
            .stateInDelayed(UiState.Loading(ProfileScreenData()), viewModelScope)

    fun signOut() {
        viewModelScope.launch {
            profileDataRepository.signOut()
        }
    }
}
