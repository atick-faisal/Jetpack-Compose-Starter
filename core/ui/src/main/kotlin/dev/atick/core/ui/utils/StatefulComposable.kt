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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.atick.core.ui.components.JetpackOverlayLoadingWheel

@Composable
fun <T : Any> StatefulComposable(
    state: UiState<T>,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    content: @Composable (T) -> Unit,
) {
    content(state.data)

    if (state.isLoading) {
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

    if (state.error != null) {
        LaunchedEffect(onShowSnackbar) {
            onShowSnackbar(state.error.message.toString(), null)
        }
    }
}

data class UiState<T : Any>(
    val data: T,
    val loading: Boolean = false,
    val error: Throwable? = null,
) {
    val isLoading: Boolean
        get() = loading && error == null
}
