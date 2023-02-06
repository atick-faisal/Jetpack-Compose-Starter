package dev.atick.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInDelayed(
    initialValue: T,
    scope: CoroutineScope
): StateFlow<T> {
    return this.stateIn(
        scope = scope,
        initialValue = initialValue,
        started = SharingStarted.WhileSubscribed(5000L)
    )
}