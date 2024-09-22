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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class BillingDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BillingDataSource {

    private val _products = MutableStateFlow(emptyList<Product>())
    override val products: StateFlow<List<Product>>
        get() = _products.asStateFlow()

    private val _purchases = MutableSharedFlow<List<OneTimePurchase>>()
    override val purchases: SharedFlow<List<OneTimePurchase>>
        get() = _purchases.asSharedFlow()

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                val oneTimePurchases = purchases.map { it.asOneTimePurchase() }
                _products.update { products ->
                    products.updatePurchaseState(oneTimePurchases)
                }
                _purchases.tryEmit(oneTimePurchases)
            }
        }

    private val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
        .enableOneTimeProducts()
        .build()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(pendingPurchasesParams)
        .setListener(purchasesUpdatedListener)
        .build()

    override suspend fun initializeBilling() {
        initializeGooglePlayBilling()
        updateProductsAndPurchases()
    }

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

    private suspend fun updateProductsAndPurchases() {
        Timber.d("Updating Purchases ... ")
        val products = getProducts()
        Timber.d("Products: $products")
        val purchases = getPurchases()
        Timber.d("Purchases: $purchases")
        val updatedProducts = products.updatePurchaseState(purchases)
        Timber.d("Updated Products: $products")
        verifyAndAcknowledgePurchases(purchases)
        _products.update { updatedProducts }
    }

    override suspend fun verifyAndAcknowledgePurchases(purchases: List<OneTimePurchase>) {
        purchases.forEach { purchase ->
            if ((purchase.oneTimePurchaseState == OneTimePurchaseState.Purchased) &&
                !purchase.isAcknowledged
            ) {
                verifyPurchase(purchase)
                acknowledgePurchase(purchase)
            }
        }
    }

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

    private suspend fun verifyPurchase(purchase: OneTimePurchase) {
        suspendCancellableCoroutine { continuation ->
            continuation.resume(Unit)
        }
    }

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

    private suspend fun getProducts(): List<Product> {
        val params = getProductQueryParams(JetpackProducts)
        return getProductDetailsList(params).map { it.asProduct() }
    }

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
                    } else continuation.resume(emptyList())

                }
                billingClient.queryPurchasesAsync(params.build(), callback)
                continuation.invokeOnCancellation {
                    billingClient.endConnection()
                }
            }
        }
    }

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
        if (billingResult.responseCode != BillingResponseCode.OK)
            throw Exception("Purchase Failed")
    }
}