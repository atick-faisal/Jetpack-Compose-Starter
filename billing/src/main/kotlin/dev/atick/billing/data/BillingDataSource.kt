package dev.atick.billing.data

import android.app.Activity
import dev.atick.billing.models.OneTimePurchase
import dev.atick.billing.models.Product
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BillingDataSource {
    val products: StateFlow<List<Product>>
    val purchases: SharedFlow<List<OneTimePurchase>>

    suspend fun initializeBilling()
    suspend fun purchaseProduct(activity: Activity, product: Product)
    suspend fun verifyAndAcknowledgePurchases(purchases: List<OneTimePurchase>)
}