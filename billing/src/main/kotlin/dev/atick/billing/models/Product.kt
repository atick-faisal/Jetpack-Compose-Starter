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

package dev.atick.billing.models

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails

/**
 * Data class for a product.
 *
 * @param id The product ID.
 * @param name The product name.
 * @param description The product description.
 * @param formattedPrice The formatted price.
 * @param productType The product type.
 * @param oneTimePurchaseState The state of the one-time purchase.
 */
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val formattedPrice: String,
    val productType: ProductType = ProductType.ONE_TIME,
    val oneTimePurchaseState: OneTimePurchaseState = OneTimePurchaseState.Available,
) {
    val displayPrice = when (oneTimePurchaseState) {
        OneTimePurchaseState.Available -> formattedPrice
        OneTimePurchaseState.Purchased -> "Purchased"
        OneTimePurchaseState.Pending -> "Pending"
        OneTimePurchaseState.Verifying -> "Verifying"
        OneTimePurchaseState.Unavailable -> "Unavailable"
    }
    val purchasable: Boolean = oneTimePurchaseState == OneTimePurchaseState.Available
}

/**
 * Enum class for the product type.
 */
enum class ProductType {
    ONE_TIME, SUBSCRIPTION
}

/**
 * Converts a [ProductType] to a Play Store product type.
 *
 * @return The Play Store product type.
 */
fun ProductType.asPlayStoreProductType(): String {
    return when (this) {
        ProductType.ONE_TIME -> BillingClient.ProductType.INAPP
        ProductType.SUBSCRIPTION -> BillingClient.ProductType.SUBS
    }
}

/**
 * Converts a [ProductDetails] to a [Product].
 *
 * @return The [Product] object.
 */
fun ProductDetails.asProduct(): Product {
    return Product(
        id = productId,
        name = name,
        description = description,
        formattedPrice = oneTimePurchaseOfferDetails?.formattedPrice ?: "Unavailable",
    )
}
