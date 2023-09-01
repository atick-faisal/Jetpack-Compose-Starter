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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.atick.core.ui.component.JetpackOverlayLoadingWheel

@Stable
@Composable
fun <T> StatefulComposable(
    state: UiState<T>,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    content: @Composable (T) -> Unit,
) {
    content(state.data)
    when (state) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                JetpackOverlayLoadingWheel(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentDesc = "",
                )
            }
        }

        is UiState.Error -> {
            LaunchedEffect(onShowSnackbar) {
                onShowSnackbar(state.t?.message.toString(), null)
            }
        }

        else -> {}
    }
}

sealed class UiState<out T>(val data: T) {
    data class Loading<T>(val d: T) : UiState<T>(d)
    data class Success<T>(val d: T) : UiState<T>(d)
    data class Error<T>(val d: T, val t: Throwable?) : UiState<T>(d)
}
