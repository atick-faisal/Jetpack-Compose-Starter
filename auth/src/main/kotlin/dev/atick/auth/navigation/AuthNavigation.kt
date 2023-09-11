package dev.atick.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.atick.auth.ui.signin.SignInRoute
import dev.atick.auth.ui.signup.SignUpRoute

const val signInNavigationRoute = "sign_in"
const val signUpNavigationRoute = "sign_up"
const val authNavGraphRoute = "auth_graph"

fun NavController.navigateToAuthNavGraph(navOptions: NavOptions? = null) {
    navigate(authNavGraphRoute, navOptions)
}

fun NavController.navigateToSignInRoute(navOptions: NavOptions? = null) {
    navigate(signInNavigationRoute, navOptions)
}

fun NavController.navigateToSignUpRoute(navOptions: NavOptions? = null) {
    navigate(signUpNavigationRoute, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    navigation(
        route = authNavGraphRoute,
        startDestination = signInNavigationRoute,
    ) {
        composable(signInNavigationRoute) {
            SignInRoute(
                onSignUpClick = onSignUpClick,
                onShowSnackbar = onShowSnackbar,
            )
        }
        composable(signUpNavigationRoute) {
            SignUpRoute(onShowSnackbar = onShowSnackbar)
        }
    }
}