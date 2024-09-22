package dev.atick.billing.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.billing.models.BillingScreenData
import dev.atick.billing.models.Product
import dev.atick.billing.repository.BillingRepository
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
) : ViewModel() {

    private val _billingUiState: MutableStateFlow<UiState<BillingScreenData>> =
        MutableStateFlow(UiState(BillingScreenData()))
    val billingUiState = _billingUiState.asStateFlow()

    init {
        billingRepository.products
            .onEach { products ->
                _billingUiState.update { UiState(it.data.copy(products = products)) }
            }
            .launchIn(viewModelScope)

        billingRepository.purchases
            .onEach {
                billingRepository.verifyAndAcknowledgePurchases(it)
            }
            .launchIn(viewModelScope)
        initializeBilling()
    }

    private fun initializeBilling() {
        _billingUiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val result = billingRepository.initializeBilling()
            if (result.isSuccess) {
                _billingUiState.update { it.copy(loading = false) }
            } else {
                _billingUiState.update { it.copy(error = result.exceptionOrNull()) }
            }
        }
    }

    fun purchaseProduct(activity: Activity, product: Product) {
        _billingUiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val result = billingRepository.purchaseProduct(activity, product)
            if (result.isSuccess) {
                _billingUiState.update { it.copy(loading = false) }
            } else {
                _billingUiState.update { it.copy(error = result.exceptionOrNull()) }
            }
        }
    }
}