package dev.atick.billing.data

import android.app.Activity
import dev.atick.billing.models.Product
import kotlinx.coroutines.flow.StateFlow

interface BillingDataSource {
    val products: StateFlow<List<Product>>

    suspend fun updateProductsAndPurchases()
    suspend fun purchaseProduct(activity: Activity, product: Product)
}