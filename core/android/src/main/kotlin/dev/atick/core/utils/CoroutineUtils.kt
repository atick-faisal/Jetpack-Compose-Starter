package dev.atick.core.utils

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.time.Duration

suspend inline fun <T> suspendCoroutineWithTimeout(
    timeout: Duration,
    crossinline block: (Continuation<T>) -> Unit
): T {
    return withTimeout(timeout) {
        suspendCancellableCoroutine(block)
    }
}

suspend inline fun <T> suspendCoroutineWithTimeout(
    timeMillis: Long,
    crossinline block: (CancellableContinuation<T>) -> Unit
): T {
    return withTimeout(timeMillis) {
        suspendCancellableCoroutine(block)
    }
}

