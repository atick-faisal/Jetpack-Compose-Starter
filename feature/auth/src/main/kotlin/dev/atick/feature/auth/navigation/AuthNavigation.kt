/*
 * Copyright 2023 Atick Faisal
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

package dev.atick.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.atick.core.ui.utils.SnackbarAction
import dev.atick.feature.auth.ui.signin.SignInScreen
import dev.atick.feature.auth.ui.signup.SignUpScreen
import kotlinx.serialization.Serializable

/**
 * Auth navigation graph.
 */
@Serializable
data object AuthNavGraph

/**
 * Sign in route.
 */
@Serializable
data object SignIn

/**
 * Sign up route.
 */
@Serializable
data object SignUp

/**
 * Navigate to the auth navigation graph.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToAuthNavGraph(navOptions: NavOptions? = null) {
    navigate(AuthNavGraph, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToSignInScreen(navOptions: NavOptions? = null) {
    navigate(SignIn, navOptions)
}

/**
 * Navigate to the sign up route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToSignUpScreen(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

/**
 * Sign in screen.
 *
 * @param onSignUpClick Callback when sign up is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signInScreen(
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignIn> {
        SignInScreen(
            onSignUpClick = onSignUpClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Sign up screen.
 *
 * @param onSignInClick Callback when sign in is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signUpScreen(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignUp> {
        SignUpScreen(
            onSignInClick = onSignInClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Auth navigation graph.
 *
 * @param nestedNavGraphs Nested navigation graphs.
 */
fun NavGraphBuilder.authNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<AuthNavGraph>(startDestination = SignIn) {
        nestedNavGraphs()
    }
}
