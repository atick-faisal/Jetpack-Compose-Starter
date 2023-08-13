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

package dev.atick.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable

@Stable
@Composable
fun <T> StatefulComposable(
    state: UiState<T>,
    onShowLoadingDialog: (Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is UiState.Loading -> { onShowLoadingDialog(true) }
        is UiState.Success -> {
            onShowLoadingDialog(false)
            content(state.data)
        }
        is UiState.Error -> {
            onShowLoadingDialog(false)
            LaunchedEffect(onShowSnackbar) {
                onShowSnackbar(state.exception.message.toString(), null)
            }
        }
    }
}

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error<T>(val exception: Exception, val data: T? = null) : UiState<T>
}
