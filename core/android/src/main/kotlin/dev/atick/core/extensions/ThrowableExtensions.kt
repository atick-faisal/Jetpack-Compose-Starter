/*
 * Copyright 2024 Atick Faisal
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

package dev.atick.core.extensions

import dev.atick.core.utils.OneTimeEvent

/**
 * Extension function to get the stack trace of a Throwable as a string.
 *
 * @receiver Throwable The throwable instance.
 * @return A string representation of the stack trace.
 */
fun Throwable.getStackTraceString(): String {
    return stackTrace.joinToString("\n")
}

/**
 * Extension function to convert a Throwable into a OneTimeEvent.
 *
 * @receiver Throwable The throwable instance.
 * @return A OneTimeEvent containing the throwable.
 */
fun Throwable.asOneTimeEvent(): OneTimeEvent<Throwable?> {
    return OneTimeEvent(this)
}
