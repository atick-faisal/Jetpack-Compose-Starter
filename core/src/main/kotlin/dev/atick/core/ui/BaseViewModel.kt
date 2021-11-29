package dev.atick.core.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.atick.core.utils.Event

open class BaseViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<Event<String>>()
    private val toastMessage: LiveData<Event<String>>
        get() = _toastMessage
}