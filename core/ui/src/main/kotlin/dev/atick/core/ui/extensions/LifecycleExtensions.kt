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

package dev.atick.core.ui.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.atick.core.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Observe a [LiveData] and execute an action when the value is changed.
 *
 * @param action The action to be executed when the value is changed.
 */
inline fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    crossinline action: (T) -> Unit,
) {
    liveData.observe(this) { value ->
        value?.let { action(value) }
    }
}

/**
 * Observe a [LiveData] and execute an action when the value is changed.
 *
 * @param action The action to be executed when the value is changed.
 */
inline fun <T> LifecycleOwner.observeEvent(
    liveData: LiveData<SingleLiveEvent<T>>,
    crossinline action: (T) -> Unit,
) {
    liveData.observe(this) {
        it?.getContentIfNotHandled()?.let(action)
    }
}

/**
 * Observe a [LiveData] and execute an action when the value is changed.
 *
 * @param action The action to be executed when the value is changed.
 */
inline fun <T> LifecycleOwner.observeEvent(
    liveData: MutableLiveData<SingleLiveEvent<T>>,
    crossinline action: (T) -> Unit,
) {
    liveData.observe(this) {
        it?.getContentIfNotHandled()?.let(action)
    }
}

/**
 * Observe a [Flow] and execute an action when the value is changed.
 *
 * @param action The action to be executed when the value is changed.
 */
inline fun <T> LifecycleOwner.collectWithLifecycle(
    flow: Flow<T>,
    crossinline action: (T) -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                it?.let { action(it) }
            }
        }
    }
}
