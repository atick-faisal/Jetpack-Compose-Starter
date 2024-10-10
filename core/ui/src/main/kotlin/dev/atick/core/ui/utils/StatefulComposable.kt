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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dev.atick.core.ui.components.JetpackOverlayLoadingWheel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun <T : Any> StatefulComposable(
    state: UiState<T>,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    content: @Composable (T) -> Unit,
) {
    content(state.data)

    if (state.loading) {
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

    state.error.getContentIfNotHandled()?.let { error ->
        LaunchedEffect(onShowSnackbar) {
            val report = onShowSnackbar(error.message.toString(), "REPORT")
            if (report) Firebase.crashlytics.recordException(error)
        }
    }
}

data class UiState<T : Any>(
    val data: T,
    val loading: Boolean = false,
    val error: OneTimeEvent<Throwable?> = OneTimeEvent(null),
)

inline fun <T : Any> MutableStateFlow<UiState<T>>.updateState(update: T.() -> T) {
    update { UiState(update(it.data)) }
}

context(ViewModel)
inline fun <reified T : Any> MutableStateFlow<UiState<T>>.updateStateWith(
    crossinline operation: suspend () -> Result<T>,
) {
    if (value.loading) return
    viewModelScope.launch {
        update { it.copy(loading = true, error = OneTimeEvent(null)) }

        val result = operation()

        if (result.isSuccess) {
            result.getOrNull()?.let { data ->
                update { it.copy(data = data, loading = false) }
            } ?: {
                update { it.copy(loading = false) }
            }
        } else {
            update {
                it.copy(
                    error = OneTimeEvent(result.exceptionOrNull()),
                    loading = false,
                )
            }
        }
    }
}

context(ViewModel)
inline fun <T : Any> MutableStateFlow<UiState<T>>.updateWith(
    crossinline operation: suspend () -> Result<Unit>,
) {
    if (value.loading) return
    viewModelScope.launch {
        update { it.copy(loading = true, error = OneTimeEvent(null)) }

        val result = operation()

        if (result.isSuccess) {
            update { it.copy(loading = false) }
        } else {
            update {
                it.copy(
                    error = OneTimeEvent(result.exceptionOrNull()),
                    loading = false,
                )
            }
        }
    }
}

class OneTimeEvent<T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}
