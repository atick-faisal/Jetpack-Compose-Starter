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

package dev.atick.core.utils

import java.util.concurrent.atomic.AtomicBoolean

// Event wrapper for Single Events
// Details here: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150

/**
 * A wrapper for data that is exposed via a LiveData that represents an event.
 *
 * @param T The type of the content.
 * @property content The content of the event.
 */
class OneTimeEvent<T>(private val content: T) {
    private var hasBeenHandled = AtomicBoolean(false)

    /**
     * Returns the content if it has not been handled yet, and marks it as handled.
     *
     * @return The content if it has not been handled, otherwise null.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled.compareAndSet(false, true)) content else null
    }

    /**
     * Returns the content, even if it has already been handled.
     *
     * @return The content.
     */
    fun peekContent(): T = content
}
