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
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dev.atick.core.ui.components.JetpackOverlayLoadingWheel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

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

// TODO: context params will be available after kotlin 2.2
// until then, have to pass scope as a param
// context(ViewModel)
inline fun <reified T : Any> MutableStateFlow<UiState<T>>.updateStateWith(
    scope: CoroutineScope,
    crossinline operation: suspend () -> Result<T>,
) {
    if (value.loading) return
    scope.launch {
        update { it.copy(loading = true, error = OneTimeEvent(null)) }

        val result = operation()

        if (result.isSuccess) {
            val data = result.getOrNull()
            if (data != null) {
                update { it.copy(data = data, loading = false) }
            } else {
                update {
                    it.copy(
                        loading = false,
                        error = OneTimeEvent(
                            IllegalStateException("Operation succeeded but returned no data"),
                        ),
                    )
                }
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

// TODO: context params will be available after kotlin 2.2
// until then, have to pass scope as a param
// context(ViewModel)
inline fun <T : Any> MutableStateFlow<UiState<T>>.updateWith(
    scope: CoroutineScope,
    crossinline operation: suspend () -> Result<Unit>,
) {
    if (value.loading) return
    scope.launch {
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
    private var hasBeenHandled = AtomicBoolean(false)

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled.compareAndSet(false, true)) content else null
    }

    fun peekContent(): T = content
}
