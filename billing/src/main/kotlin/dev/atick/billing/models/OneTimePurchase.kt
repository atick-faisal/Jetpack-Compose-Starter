package dev.atick.billing.models

import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState

data class OneTimePurchase(
    val productIds: List<String>,
    val purchaseTime: Long,
    val purchaseToken: String,
    val isAcknowledged: Boolean = false,
    val oneTimePurchaseState: OneTimePurchaseState,
)

sealed interface OneTimePurchaseState {
    data object Purchased : OneTimePurchaseState
    data object Pending : OneTimePurchaseState
    data object Verifying : OneTimePurchaseState
    data object Available : OneTimePurchaseState
    data object Unavailable: OneTimePurchaseState
}

fun Purchase.asOneTimePurchase(): OneTimePurchase {
    return OneTimePurchase(
        productIds = products,
        purchaseTime = purchaseTime,
        purchaseToken = purchaseToken,
        isAcknowledged = isAcknowledged,
        oneTimePurchaseState = when (purchaseState) {
            PurchaseState.PURCHASED -> {
                if (isAcknowledged) OneTimePurchaseState.Purchased
                else OneTimePurchaseState.Verifying
            }
            PurchaseState.UNSPECIFIED_STATE -> OneTimePurchaseState.Unavailable
            PurchaseState.PENDING -> OneTimePurchaseState.Pending
            else -> OneTimePurchaseState.Available
        },
    )
}