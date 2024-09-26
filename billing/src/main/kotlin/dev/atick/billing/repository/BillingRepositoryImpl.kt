package dev.atick.billing.repository

import android.app.Activity
import dev.atick.billing.data.BillingDataSource
import dev.atick.billing.models.Product
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor(
    private val billingDataSource: BillingDataSource,
) : BillingRepository {
    override val products: StateFlow<List<Product>>
        get() = billingDataSource.products

    override suspend fun updateProductsAndPurchases(): Result<Unit> {
        return runCatching {
            billingDataSource.updateProductsAndPurchases()
        }
    }

    override suspend fun purchaseProduct(activity: Activity, product: Product): Result<Unit> {
        return runCatching {
            billingDataSource.purchaseProduct(activity, product)
        }
    }
}