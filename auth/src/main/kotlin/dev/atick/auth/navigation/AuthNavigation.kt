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

package dev.atick.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.atick.auth.ui.signin.SignInRoute
import dev.atick.auth.ui.signup.SignUpRoute
import kotlinx.serialization.Serializable

@Serializable
data object AuthNavGraph

@Serializable
data object SignIn

@Serializable
data object SignUp

fun NavController.navigateToAuthNavGraph(navOptions: NavOptions? = null) {
    navigate(AuthNavGraph, navOptions)
}

fun NavController.navigateToSignInRoute(navOptions: NavOptions? = null) {
    navigate(SignIn, navOptions)
}

fun NavController.navigateToSignUpRoute(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    onSignUpClick: () -> Unit,
    onSignInCLick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    navigation<AuthNavGraph>(startDestination = SignIn) {
        composable<SignIn> {
            SignInRoute(
                onSignUpClick = onSignUpClick,
                onShowSnackbar = onShowSnackbar,
            )
        }
        composable<SignUp> {
            SignUpRoute(
                onSignInClick = onSignInCLick,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}
