package dev.atick.compose.navigation.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.atick.compose.ui.details.DetailsRoute

internal const val postIdArg = "postId"

fun NavController.navigateToDetailsScreen(postId: Int) {
    navigate("details/$postId") { launchSingleTop = true }
}

fun NavGraphBuilder.detailsScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(
        route = "details/{$postIdArg}",
        arguments = listOf(
            navArgument(postIdArg) { type = NavType.IntType },
        ),
    ) {
        DetailsRoute(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}