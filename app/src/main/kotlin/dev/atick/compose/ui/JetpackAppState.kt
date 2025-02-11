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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import dev.atick.compose.navigation.TopLevelDestination
import dev.atick.core.extensions.stateInDelayed
import dev.atick.core.network.utils.NetworkState
import dev.atick.core.network.utils.NetworkUtils
import dev.atick.feature.home.navigation.navigateToHomeNavGraph
import dev.atick.feature.home.navigation.navigateToItemScreen
import dev.atick.feature.profile.navigation.navigateToProfileScreen
import dev.atick.firebase.analytics.utils.CrashReporter
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
 * @param crashReporter Utility for reporting exceptions.
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
    crashReporter: CrashReporter,
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
        crashReporter,
    ) {
        JetpackAppState(
            isUserLoggedIn = isUserLoggedIn,
            userProfilePictureUri = userProfilePictureUri,
            navController = navController,
            windowSizeClass = windowSizeClass,
            crashReporter = crashReporter,
            coroutineScope = coroutineScope,
            networkUtils = networkUtils,
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
 * @property crashReporter Utility for reporting exceptions.
 * @param coroutineScope The coroutine scope for managing coroutines.
 * @param networkUtils Utility for network state management.
 */
@Stable
class JetpackAppState(
    val isUserLoggedIn: Boolean,
    val userProfilePictureUri: String?,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val crashReporter: CrashReporter,
    coroutineScope: CoroutineScope,
    networkUtils: NetworkUtils,
) {
    /**
     * The previous navigation destination.
     */
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    /**
     * The current navigation destination.
     */
    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    /**
     * The current top-level navigation destination.
     */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    /**
     * Indicates if the application is offline.
     */
    val isOffline = networkUtils.getCurrentState()
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
     * Indicates if the FAB should be shown.
     */
    val shouldShowFab: Boolean
        @Composable get() = currentTopLevelDestination == TopLevelDestination.HOME

    /**
     * Navigates to the item screen with no item ID (creates new item).
     */
    fun navigateToItemScreen() {
        navController.navigateToItemScreen(null)
    }

    /**
     * Navigates to the specified top-level destination.
     *
     * @param topLevelDestination The top-level destination to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHomeNavGraph(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfileScreen(topLevelNavOptions)
        }
    }
}
