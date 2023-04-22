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

package dev.atick.compose.ui.home.state

import dev.atick.compose.data.home.Item
import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    override val loading: Boolean = false,
    override val toastMessage: UiText? = null,
    val item: Item? = null,
) : BaseUiState()

fun MutableStateFlow<HomeUiState>.setLoading(loading: Boolean) {
    update { it.copy(loading = loading) }
}

fun MutableStateFlow<HomeUiState>.setToastMessage(message: String?) {
    if (message == null) {
        update { it.copy(toastMessage = null) }
    } else {
        update { it.copy(toastMessage = UiText.DynamicString(message)) }
    }
}
