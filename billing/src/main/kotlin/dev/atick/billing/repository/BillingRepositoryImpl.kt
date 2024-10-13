/*
 * Copyright 2024 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.billing.repository

import android.app.Activity
import dev.atick.billing.data.BillingDataSource
import dev.atick.billing.models.Product
import dev.atick.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Implementation of the [BillingRepository] interface.
 *
 * @param billingDataSource The data source for billing.
 * @see BillingRepository
 */
class BillingRepositoryImpl @Inject constructor(
    private val billingDataSource: BillingDataSource,
) : BillingRepository {
    /**
     * Flow of available products.
     *
     * @return The flow of available [Product]s.
     */
    override val products: StateFlow<List<Product>>
        get() = billingDataSource.products

    /**
     * Updates the products and purchases.
     *
     * @return A [Result] of [Unit].
     */
    override suspend fun updateProductsAndPurchases(): Result<Unit> {
        return suspendRunCatching {
            billingDataSource.updateProductsAndPurchases()
        }
    }

    /**
     * Purchase a product.
     *
     * @param activity The activity instance.
     * @param product The product to purchase.
     * @return A [Result] of [Unit].
     */
    override suspend fun purchaseProduct(activity: Activity, product: Product): Result<Unit> {
        return suspendRunCatching {
            billingDataSource.purchaseProduct(activity, product)
        }
    }
}
