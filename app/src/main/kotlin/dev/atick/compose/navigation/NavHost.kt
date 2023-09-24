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
import dev.atick.auth.navigation.authNavGraph
import dev.atick.auth.navigation.authNavGraphRoute
import dev.atick.auth.navigation.navigateToSignInRoute
import dev.atick.auth.navigation.navigateToSignUpRoute
import dev.atick.compose.navigation.details.detailsScreen
import dev.atick.compose.navigation.details.navigateToDetailsScreen
import dev.atick.compose.navigation.home.homeNavGraph
import dev.atick.compose.navigation.home.homeNavGraphRoute
import dev.atick.compose.navigation.profile.profileScreen
import dev.atick.compose.ui.JetpackAppState

@Composable
fun JetpackNavHost(
    appState: JetpackAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val startDestination = if (appState.isUserLoggedIn) homeNavGraphRoute else authNavGraphRoute
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(
            onSignUpClick = navController::navigateToSignUpRoute,
            onSignInCLick = navController::navigateToSignInRoute,
            onShowSnackbar = onShowSnackbar,
        )
        homeNavGraph(
            onPostClick = navController::navigateToDetailsScreen,
            onShowSnackbar = onShowSnackbar,
            nestedNavGraphs = {
                detailsScreen(
                    onBackClick = navController::popBackStack,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
        profileScreen(onShowSnackbar = onShowSnackbar)
    }
}
