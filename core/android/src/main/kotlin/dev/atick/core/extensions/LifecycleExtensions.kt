package dev.atick.core.extensions

import androidx.lifecycle.*
import dev.atick.core.data.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    crossinline action: (T) -> Unit
) {
    liveData.observe(this, Observer { it?.let(action) })
}

inline fun <T> LifecycleOwner.observeEvent(
    liveData: LiveData<SingleLiveEvent<T>>,
    crossinline action: (T) -> Unit
) {
    liveData.observe(this, Observer {
        it?.getContentIfNotHandled()?.let(action)
    })
}

inline fun <T> LifecycleOwner.observeEvent(
    liveData: MutableLiveData<SingleLiveEvent<T>>,
    crossinline action: (T) -> Unit
) {
    liveData.observe(this, Observer {
        it?.getContentIfNotHandled()?.let(action)
    })
}

inline fun <T> LifecycleOwner.collectWithLifecycle(
    flow: Flow<T>,
    crossinline action: (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                it?.let { action(it) }
            }
        }
    }
}