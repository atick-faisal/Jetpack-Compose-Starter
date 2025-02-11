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

package dev.atick.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.atick.core.ui.utils.SnackbarAction
import dev.atick.feature.home.ui.home.HomeScreen
import dev.atick.feature.home.ui.item.ItemScreen
import kotlinx.serialization.Serializable

/**
 * Home navigation.
 */
@Serializable
data object Home

/**
 * Home navigation graph.
 */
@Serializable
data object HomeNavGraph

/**
 * Item navigation.
 */
@Serializable
data class Item(val itemId: String?)

/**
 * Navigates to the Home navigation graph.
 *
 * @param navOptions Optional navigation options to configure the navigation behavior.
 */
fun NavController.navigateToHomeNavGraph(navOptions: NavOptions? = null) {
    navigate(HomeNavGraph, navOptions)
}

/**
 * Navigates to the Item screen.
 *
 * @param itemId The item ID.
 */
fun NavController.navigateToItemScreen(itemId: String?) {
    navigate(Item(itemId)) { launchSingleTop = true }
}

/**
 * Home screen.
 *
 * @param onJetpackClick The click listener for the jetpack.
 * @param onShowSnackbar The snackbar listener.
 */
fun NavGraphBuilder.homeScreen(
    onJetpackClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Home> {
        HomeScreen(
            onJetpackClick = onJetpackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Item screen.
 *
 * @param onBackClick The back click listener.
 * @param onShowSnackbar The snackbar listener.
 */
fun NavGraphBuilder.itemScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Item> {
        ItemScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Home navigation graph.
 *
 * @param nestedNavGraphs The nested navigation graphs.
 */
fun NavGraphBuilder.homeNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<HomeNavGraph>(startDestination = Home) {
        nestedNavGraphs()
    }
}
