package dev.atick.billing.repository

import android.app.Activity
import dev.atick.billing.models.Product
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val products: StateFlow<List<Product>>

    suspend fun updateProductsAndPurchases(): Result<Unit>
    suspend fun purchaseProduct(activity: Activity, product: Product): Result<Unit>
}