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

package dev.atick.billing.data

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.billing.config.JetpackProducts
import dev.atick.billing.models.OneTimePurchase
import dev.atick.billing.models.OneTimePurchaseState
import dev.atick.billing.models.Product
import dev.atick.billing.models.ProductType
import dev.atick.billing.models.asOneTimePurchase
import dev.atick.billing.models.asPlayStoreProductType
import dev.atick.billing.models.asProduct
import dev.atick.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Implementation of [BillingDataSource] that uses Google Play Billing.
 *
 * @param context The application context.
 * @param ioDispatcher The [CoroutineDispatcher] for I/O operations.
 *
 * @see BillingDataSource
 */
class BillingDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BillingDataSource {

    /**
     * Flow of available products.
     */
    private val _products = MutableStateFlow(emptyList<Product>())
    override val products: StateFlow<List<Product>>
        get() = _products.asStateFlow()

    /**
     * Listener for purchases updated.
     */
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                val oneTimePurchases = purchases.map { it.asOneTimePurchase() }
                _products.update { products ->
                    products.updatePurchaseState(oneTimePurchases)
                }
            }
        }

    /**
     * Parameters for pending purchases.
     */
    private val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
        .enableOneTimeProducts()
        .build()

    /**
     * The [BillingClient] instance.
     */
    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(pendingPurchasesParams)
        .setListener(purchasesUpdatedListener)
        .build()

    /**
     * Initializes Google Play Billing.
     */
    private suspend fun initializeGooglePlayBilling() {
        return suspendCancellableCoroutine { continuation ->
            val billingClientStateListener =
                object : BillingClientStateListener {
                    override fun onBillingServiceDisconnected() {
                        // TODO: Resume Connection here
                        continuation.cancel(Exception("Google Play Billing Not Available"))
                    }

                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            Timber.d("Billing Client Connected: ${billingClient.isReady}")
                            continuation.resume(Unit)
                        } else {
                            continuation.cancel(Exception("Google Play Billing Not Available"))
                        }
                    }
                }
            billingClient.startConnection(billingClientStateListener)
            continuation.invokeOnCancellation {
                billingClient.endConnection()
            }
        }
    }

    /**
     * Updates the products and purchases.
     */
    override suspend fun updateProductsAndPurchases() {
        if (!billingClient.isReady) {
            initializeGooglePlayBilling()
        }
        Timber.d("Updating Purchases ... ")
        val products = getProducts()
        val purchases = getPurchases()
        verifyAndAcknowledgePurchases(products, purchases)
        Timber.d("Products: $products")
        Timber.d("Purchases: $purchases")
    }

    /**
     * Verifies and acknowledges purchases.
     *
     * @param products The list of products.
     * @param purchases The list of purchases.
     */
    private suspend fun verifyAndAcknowledgePurchases(
        products: List<Product>,
        purchases: List<OneTimePurchase>,
    ) {
        Timber.d("Acknowledging Purchases ... ")
        val updatedPurchases = purchases.map { purchase ->
            if (purchase.oneTimePurchaseState == OneTimePurchaseState.Verifying) {
                verifyPurchase(purchase)
                acknowledgePurchase(purchase)
                purchase.copy(
                    isAcknowledged = true,
                    oneTimePurchaseState = OneTimePurchaseState.Purchased,
                )
            } else {
                purchase
            }
        }
        _products.update {
            products.updatePurchaseState(updatedPurchases)
        }
    }

    /**
     * Acknowledges a purchase.
     *
     * @param purchase The purchase to acknowledge.
     */
    private suspend fun acknowledgePurchase(purchase: OneTimePurchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        return suspendCancellableCoroutine { continuation ->
            val acknowledgePurchaseResponseListener =
                AcknowledgePurchaseResponseListener { billingResult ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        continuation.resume(Unit)
                    } else {
                        continuation.cancel(Exception("Error Acknowledging Purchase"))
                    }
                }

            billingClient.acknowledgePurchase(
                acknowledgePurchaseParams,
                acknowledgePurchaseResponseListener,
            )

            continuation.invokeOnCancellation { billingClient.endConnection() }
        }
    }

    /**
     * Verifies a purchase.
     *
     * @param purchase The purchase to verify.
     */
    @Suppress("UNUSED_PARAMETER")
    private suspend fun verifyPurchase(purchase: OneTimePurchase) {
        // TODO: Implement Purchase Verification
        suspendCancellableCoroutine { continuation ->
            continuation.resume(Unit)
        }
    }

    /**
     * Updates the purchase state of products.
     *
     * @param purchases The list of purchases.
     * @return The updated list of products.
     */
    private fun List<Product>.updatePurchaseState(purchases: List<OneTimePurchase>): List<Product> {
        return map { product ->
            val matchingPurchase =
                purchases.find { purchase -> purchase.productIds.contains(product.id) }
            if (matchingPurchase != null) {
                product.copy(oneTimePurchaseState = matchingPurchase.oneTimePurchaseState)
            } else {
                product
            }
        }
    }

    /**
     * Gets the list of products.
     *
     * @return The list of products.
     */
    private suspend fun getProducts(): List<Product> {
        val params = getProductQueryParams(JetpackProducts)
        return getProductDetailsList(params).map { it.asProduct() }
    }

    /**
     * Gets the list of product details.
     *
     * @param params The query parameters.
     * @return The list of product details.
     */
    private suspend fun getProductDetailsList(
        params: QueryProductDetailsParams,
    ): List<ProductDetails> {
        val productDetailsResult = withContext(ioDispatcher) {
            billingClient.queryProductDetails(params)
        }

        val billingResultCode = productDetailsResult.billingResult.responseCode
        val products = productDetailsResult.productDetailsList

        return if (billingResultCode == BillingResponseCode.OK && products != null) {
            products
        } else {
            emptyList()
        }
    }

    /**
     * Gets the list of purchases.
     *
     * @return The list of purchases.
     */
    private suspend fun getPurchases(): List<OneTimePurchase> {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        return withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                val callback = PurchasesResponseListener { billingResult, purchases ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        continuation.resume(
                            purchases.map {
                                Timber.d("Purchased Products: ${it.products}")
                                it.asOneTimePurchase()
                            },
                        )
                    } else {
                        continuation.resume(emptyList())
                    }
                }
                billingClient.queryPurchasesAsync(params.build(), callback)
                continuation.invokeOnCancellation {
                    billingClient.endConnection()
                }
            }
        }
    }

    /**
     * Gets the query parameters for products.
     *
     * @param products The map of product IDs and types.
     * @return The query parameters.
     */
    private fun getProductQueryParams(
        products: Map<String, ProductType>,
    ): QueryProductDetailsParams {
        val productList = products.map { (id, type) ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(type.asPlayStoreProductType())
                .build()
        }
        return QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
    }

    /**
     * Purchases a product.
     *
     * @param activity The activity instance.
     * @param product The product to purchase.
     */
    override suspend fun purchaseProduct(activity: Activity, product: Product) {
        val products = mapOf(product.id to product.productType)
        val params = getProductQueryParams(products)

        val productDetailsList = getProductDetailsList(params)

        val matchingPurchase = productDetailsList.find { productDetails ->
            productDetails.productId == product.id
        }

        matchingPurchase ?: throw Exception("Product Not Found")

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(matchingPurchase)
                .build(),
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
        if (billingResult.responseCode != BillingResponseCode.OK) {
            throw Exception("Purchase Failed")
        }
    }
}
