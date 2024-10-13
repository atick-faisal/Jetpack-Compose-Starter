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

package dev.atick.auth.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.auth.models.AuthScreenData
import dev.atick.auth.repository.AuthRepository
import dev.atick.core.extensions.isEmailValid
import dev.atick.core.extensions.isPasswordValid
import dev.atick.core.extensions.isValidFullName
import dev.atick.core.ui.utils.TextFiledData
import dev.atick.core.ui.utils.UiState
import dev.atick.core.ui.utils.updateState
import dev.atick.core.ui.utils.updateWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authUiState = MutableStateFlow(UiState(AuthScreenData()))
    val authUiState = _authUiState.asStateFlow()

    fun updateName(name: String) {
        _authUiState.updateState {
            copy(
                name = TextFiledData(
                    value = name,
                    errorMessage = if (name.isValidFullName()) null else "Name Not Valid",
                ),
            )
        }
    }

    fun updateEmail(email: String) {
        _authUiState.updateState {
            copy(
                email = TextFiledData(
                    value = email,
                    errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                ),
            )
        }
    }

    fun updatePassword(password: String) {
        _authUiState.updateState {
            copy(
                password = TextFiledData(
                    value = password,
                    errorMessage = if (password.isPasswordValid()) null else "Password Not Valid",
                ),
            )
        }
    }

    fun signInWithSavedCredentials(activity: Activity) {
        _authUiState.updateWith(viewModelScope) {
            authRepository.signInWithSavedCredentials(activity)
        }
    }

    fun loginWithEmailAndPassword() {
        _authUiState.updateWith(viewModelScope) {
            authRepository.signInWithEmailAndPassword(
                email = authUiState.value.data.email.value,
                password = authUiState.value.data.password.value,
            )
        }
    }

    fun registerWithEmailAndPassword(activity: Activity) {
        _authUiState.updateWith(viewModelScope) {
            authRepository.registerWithEmailAndPassword(
                name = authUiState.value.data.name.value,
                email = authUiState.value.data.email.value,
                password = authUiState.value.data.password.value,
                activity = activity,
            )
        }
    }

    fun signInWithGoogle(activity: Activity) {
        _authUiState.updateWith(viewModelScope) { authRepository.signInWithGoogle(activity) }
    }

    fun registerWithGoogle(activity: Activity) {
        _authUiState.updateWith(viewModelScope) { authRepository.registerWithGoogle(activity) }
    }
}
