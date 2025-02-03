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

package dev.atick.compose.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.atick.compose.ui.home.HomeRoute
import dev.atick.core.ui.utils.SnackbarAction
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Home screen.
 */
@Serializable
data object Home

/**
 * Extension function for [NavController] to navigate to the Home screen.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}

/**
 * Adds the Home screen to the [NavGraphBuilder].
 *
 * @param onShowSnackbar Lambda function to show a snackbar with a message and an action.
 */
fun NavGraphBuilder.homeScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Home> {
        HomeRoute(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
