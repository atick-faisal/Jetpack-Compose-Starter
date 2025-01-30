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

package dev.atick.compose.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import dev.atick.compose.navigation.TopLevelDestination
import dev.atick.compose.navigation.home.navigateToHome
import dev.atick.core.extensions.stateInDelayed
import dev.atick.network.utils.NetworkState
import dev.atick.network.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Remembers and creates an instance of [JetpackAppState].
 *
 * @param isUserLoggedIn Indicates if the user is logged in.
 * @param windowSizeClass The current window size class.
 * @param networkUtils Utility for network state management.
 * @param userProfilePictureUri The URI of the user's profile picture.
 * @param coroutineScope The coroutine scope for managing coroutines.
 * @param navController The navigation controller for managing navigation.
 * @return An instance of [JetpackAppState].
 */
@Composable
fun rememberJetpackAppState(
    isUserLoggedIn: Boolean,
    windowSizeClass: WindowSizeClass,
    networkUtils: NetworkUtils,
    userProfilePictureUri: String? = null,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): JetpackAppState {
    return remember(
        isUserLoggedIn,
        userProfilePictureUri,
        navController,
        windowSizeClass,
        coroutineScope,
        networkUtils,
    ) {
        JetpackAppState(
            isUserLoggedIn,
            userProfilePictureUri,
            navController,
            windowSizeClass,
            coroutineScope,
            networkUtils,
        )
    }
}

/**
 * State holder class for the Jetpack Compose application.
 *
 * @property isUserLoggedIn Indicates if the user is logged in.
 * @property userProfilePictureUri The URI of the user's profile picture.
 * @property navController The navigation controller for managing navigation.
 * @property windowSizeClass The current window size class.
 * @property coroutineScope The coroutine scope for managing coroutines.
 * @property networkUtils Utility for network state management.
 */
@Suppress("MemberVisibilityCanBePrivate", "UNUSED")
@Stable
class JetpackAppState(
    val isUserLoggedIn: Boolean,
    val userProfilePictureUri: String?,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope,
    networkUtils: NetworkUtils,
) {
    /**
     * The current navigation destination.
     */
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    /**
     * The current top-level navigation destination.
     */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            // Getting the current back stack entry here instead of using currentDestination
            // solves the vanishing bottom bar issue
            // (https://github.com/atick-faisal/Jetpack-Compose-Starter/issues/255)
            val backStackEntry by navController.currentBackStackEntryAsState()
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                backStackEntry?.destination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    /**
     * Indicates if the bottom bar should be shown.
     */
    val shouldShowBottomBar: Boolean
        @Composable get() = (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) &&
            (currentTopLevelDestination != null)

    /**
     * Indicates if the navigation rail should be shown.
     */
    val shouldShowNavRail: Boolean
        @Composable get() = (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) &&
            (currentTopLevelDestination != null)

    /**
     * Indicates if the application is offline.
     */
    val isOffline = networkUtils.currentState
        .map { it != NetworkState.CONNECTED }
        .stateInDelayed(false, coroutineScope)

    /**
     * List of top-level destinations.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * State flow of top-level destinations with unread resources.
     */
    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
        // TODO: Requires Implementation
        MutableStateFlow(setOf<TopLevelDestination>()).asStateFlow()

    /**
     * Navigates to the specified top-level destination.
     *
     * @param topLevelDestination The top-level destination to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
        }
    }
}
