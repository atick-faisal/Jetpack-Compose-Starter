package dev.atick.billing.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onShowSnackbar: suspend (String, String?) -> Boolean,
    billingViewModel: BillingViewModel = hiltViewModel(),
) {
    val billingUiState by billingViewModel.billingUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = billingUiState,
        onShowSnackbar = onShowSnackbar,
    ) { billingScreenData ->
        BillingScreen(
            billingScreenData = billingScreenData,
            onPurchaseProduct = billingViewModel::purchaseProduct,
        )
    }
}

@Composable
fun BillingScreen(
    billingScreenData: BillingScreenData,
    onPurchaseProduct: (Activity, Product) -> Unit,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = scrollableState,
    ) {
        items(billingScreenData.products) { product ->
            ProductCard(product, onPurchaseProduct)
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
                modifier = Modifier.weight(1f)
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

@DevicePreviews
@Composable
fun BillingScreenPreview() {
    BillingScreen(
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
    )
}
