package dev.atick.billing.models

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails

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

enum class ProductType {
    ONE_TIME, SUBSCRIPTION
}

fun ProductType.asPlayStoreProductType(): String {
    return when(this) {
        ProductType.ONE_TIME -> BillingClient.ProductType.INAPP
        ProductType.SUBSCRIPTION -> BillingClient.ProductType.SUBS
    }
}

fun ProductDetails.asProduct(): Product {
    return Product(
        id = productId,
        name = name,
        description = description,
        formattedPrice = oneTimePurchaseOfferDetails?.formattedPrice ?: "Unavailable"
    )
}