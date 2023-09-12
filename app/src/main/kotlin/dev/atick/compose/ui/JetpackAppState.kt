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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import dev.atick.compose.navigation.TopLevelDestination
import dev.atick.compose.navigation.home.homeNavigationRoute
import dev.atick.compose.navigation.home.navigateToHomeNavGraph
import dev.atick.compose.navigation.profile.navigateProfile
import dev.atick.compose.navigation.profile.profileNavigationRoute
import dev.atick.core.extensions.stateInDelayed
import dev.atick.network.utils.NetworkState
import dev.atick.network.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

@Composable
fun rememberJetpackAppState(
    isUserLoggedIn: Boolean,
    windowSizeClass: WindowSizeClass,
    networkUtils: NetworkUtils,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): JetpackAppState {
    return remember(
        isUserLoggedIn,
        navController,
        windowSizeClass,
        coroutineScope,
        networkUtils,
    ) {
        JetpackAppState(
            isUserLoggedIn,
            navController,
            windowSizeClass,
            coroutineScope,
            networkUtils,
        )
    }
}

@Suppress("MemberVisibilityCanBePrivate", "UNUSED")
@Stable
class JetpackAppState(
    val isUserLoggedIn: Boolean,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope,
    networkUtils: NetworkUtils,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeNavigationRoute -> TopLevelDestination.HOME
            profileNavigationRoute -> TopLevelDestination.PROFILE
            else -> null
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) &&
            (currentTopLevelDestination != null)

    val shouldShowNavRail: Boolean
        @Composable get() = (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) &&
            (currentTopLevelDestination != null)

    val isOffline = networkUtils.currentState
        .map { it != NetworkState.CONNECTED }
        .stateInDelayed(false, coroutineScope)

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
        // TODO: Requires Implementation
        MutableStateFlow(setOf<TopLevelDestination>()).asStateFlow()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHomeNavGraph(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateProfile(topLevelNavOptions)
        }
    }
}
