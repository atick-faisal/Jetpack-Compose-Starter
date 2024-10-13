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

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/**
 * Suspends the current coroutine until the specified block is completed or the timeout is reached.
 *
 * @param timeout The duration to wait for the block to complete.
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendCoroutineWithTimeout(
    timeout: Duration,
    crossinline block: (Continuation<T>) -> Unit,
): T {
    return withTimeout(timeout) {
        suspendCancellableCoroutine(block)
    }
}

/**
 * Suspends the current coroutine until the specified block is completed or the timeout is reached.
 *
 * @param timeMillis The time in milliseconds to wait for the block to complete.
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendCoroutineWithTimeout(
    timeMillis: Long,
    crossinline block: (CancellableContinuation<T>) -> Unit,
): T {
    return withTimeout(timeMillis) {
        suspendCancellableCoroutine(block)
    }
}

/**
 * Runs the specified block and returns the result as a [Result].
 *
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}
