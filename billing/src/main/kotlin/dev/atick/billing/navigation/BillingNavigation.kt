package dev.atick.billing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.atick.billing.ui.BillingRoute
import kotlinx.serialization.Serializable

@Serializable
data object Billing

fun NavController.navigateToBilling(navOptions: NavOptions? = null) {
    navigate(Billing, navOptions)
}

fun NavGraphBuilder.billingScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<Billing> {
        BillingRoute(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}