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

package dev.atick.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.atick.auth.navigation.AuthNavGraph
import dev.atick.auth.navigation.authNavGraph
import dev.atick.auth.navigation.navigateToSignInRoute
import dev.atick.auth.navigation.navigateToSignUpRoute
import dev.atick.auth.navigation.signInScreen
import dev.atick.auth.navigation.signUpScreen
import dev.atick.compose.navigation.home.Home
import dev.atick.compose.navigation.home.homeScreen
import dev.atick.compose.ui.JetpackAppState

/**
 * Composable function that sets up the navigation host for the Jetpack Compose application.
 *
 * @param appState The state of the Jetpack application, containing the navigation controller and user login status.
 * @param onShowSnackbar A lambda function to show a snackbar with a message and optional action.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun JetpackNavHost(
    appState: JetpackAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val startDestination =
        if (appState.isUserLoggedIn) Home::class else AuthNavGraph::class
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(
            nestedNavGraphs = {
                signInScreen(
                    onSignUpClick = navController::navigateToSignUpRoute,
                    onShowSnackbar = onShowSnackbar,
                )
                signUpScreen(
                    onSignInClick = navController::navigateToSignInRoute,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
        homeScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
