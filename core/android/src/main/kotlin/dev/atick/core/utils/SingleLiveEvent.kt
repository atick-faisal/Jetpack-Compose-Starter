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

@file:Suppress("MemberVisibilityCanBePrivate")

package dev.atick.core.utils

// Event wrapper for Single Events
// Details here: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150

/**
 * A class that represents a single-use event.
 *
 * @param T The type of the event content.
 * @param content The content of the event.
 */
open class SingleLiveEvent<out T>(private val content: T) {

    /**
     * Flag indicating whether the event has been handled.
     */
    var hasBeenHandled = false
        private set

    /**
     * Returns the content if it has not been handled yet.
     *
     * @return The content of the event if it has not been handled, or null if it has been handled.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content regardless of whether it has been handled.
     *
     * @return The content of the event.
     */
    fun peekContent(): T = content
}
