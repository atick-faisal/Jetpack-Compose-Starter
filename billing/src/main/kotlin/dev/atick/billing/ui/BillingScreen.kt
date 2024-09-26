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

package dev.atick.billing.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.billing.R
import dev.atick.billing.models.BillingScreenData
import dev.atick.billing.models.OneTimePurchaseState
import dev.atick.billing.models.Product
import dev.atick.billing.models.ProductType
import dev.atick.core.extensions.getActivity
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.utils.DevicePreviews
import dev.atick.core.ui.utils.StatefulComposable

@Composable
fun BillingRoute(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    billingViewModel: BillingViewModel = hiltViewModel(),
) {
    val billingUiState by billingViewModel.billingUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = billingUiState,
        onShowSnackbar = onShowSnackbar,
    ) { billingScreenData ->
        BillingScreen(
            onBackClick = onBackClick,
            billingScreenData = billingScreenData,
            onPurchaseProduct = billingViewModel::purchaseProduct,
            onRefreshProducts = billingViewModel::updateProductsAndPurchases,
        )
    }
}

@Composable
fun BillingScreen(
    onBackClick: () -> Unit,
    billingScreenData: BillingScreenData,
    onPurchaseProduct: (Activity, Product) -> Unit,
    onRefreshProducts: () -> Unit,
) {
    val scrollableState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onRefreshProducts()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        BillingToolbar(onBackClick = onBackClick)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollableState,
        ) {
            items(billingScreenData.products) { product ->
                ProductCard(product, onPurchaseProduct)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onPurchaseProduct: (Activity, Product) -> Unit,
) {
    val context = LocalContext.current
    val activity = context.getActivity()

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.description)
            }
            Spacer(modifier = Modifier.width(16.dp))
            JetpackButton(
                onClick = {
                    activity?.let { onPurchaseProduct(activity, product) }
                },
                enabled = product.purchasable,
            ) {
                Text(product.displayPrice)
            }
        }
    }
}

@Composable
private fun BillingToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        JetpackButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.done))
        }
    }
}

@DevicePreviews
@Composable
fun BillingScreenPreview() {
    BillingScreen(
        onBackClick = { },
        billingScreenData = BillingScreenData(
            products = listOf(
                Product(
                    id = "",
                    name = "Jetpack Premium",
                    description = "Premium Features for Jetpack",
                    formattedPrice = "$3.99",
                    productType = ProductType.ONE_TIME,
                    oneTimePurchaseState = OneTimePurchaseState.Available,
                ),
                Product(
                    id = "",
                    name = "Jetpack Premium",
                    description = "Premium Features for Jetpack",
                    formattedPrice = "$3.99",
                    productType = ProductType.ONE_TIME,
                    oneTimePurchaseState = OneTimePurchaseState.Available,
                ),
                Product(
                    id = "",
                    name = "Jetpack Premium",
                    description = "Premium Features for Jetpack",
                    formattedPrice = "$3.99",
                    productType = ProductType.ONE_TIME,
                    oneTimePurchaseState = OneTimePurchaseState.Available,
                ),
            ),
        ),
        onPurchaseProduct = { _, _ -> },
        onRefreshProducts = { },
    )
}
