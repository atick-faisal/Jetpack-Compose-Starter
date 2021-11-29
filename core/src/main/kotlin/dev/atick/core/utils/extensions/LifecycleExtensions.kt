package dev.atick.core.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.atick.core.utils.Event

inline fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    crossinline action: (T) -> Unit
) {
    liveData.observe(this, { it?.let(action) })
}

inline fun <T> LifecycleOwner.observeEvent(
    liveData: MutableLiveData<Event<T>>,
    crossinline action: (T) -> Unit
) {
    liveData.observe(this, { it?.getContentIfNotHandled()?.let(action) })
}