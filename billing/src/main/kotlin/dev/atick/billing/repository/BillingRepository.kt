package dev.atick.billing.repository

import android.app.Activity
import dev.atick.billing.models.OneTimePurchase
import dev.atick.billing.models.Product
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val products: StateFlow<List<Product>>
    val purchases: SharedFlow<List<OneTimePurchase>>

    suspend fun initializeBilling(): Result<Unit>
    suspend fun purchaseProduct(activity: Activity, product: Product): Result<Unit>
    suspend fun verifyAndAcknowledgePurchases(purchases: List<OneTimePurchase>): Result<Unit>
}