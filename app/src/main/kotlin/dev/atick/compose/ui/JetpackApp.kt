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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.compose.R
import dev.atick.compose.navigation.JetpackNavHost
import dev.atick.compose.navigation.TopLevelDestination
import dev.atick.compose.ui.common.JetpackBottomBar
import dev.atick.compose.ui.common.JetpackNavRail
import dev.atick.compose.ui.common.JetpackTopAppBar
import dev.atick.network.utils.NetworkUtils

@Composable
@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
)
fun JetpackApp(
    windowSizeClass: WindowSizeClass,
    networkUtils: NetworkUtils,
    appState: JetpackAppState = rememberJetpackAppState(
        windowSizeClass = windowSizeClass,
        networkUtils = networkUtils,
    ),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val unreadDestinations by appState.topLevelDestinationsWithUnreadResources.collectAsStateWithLifecycle()
    var shouldShowLoadingDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                JetpackBottomBar(
                    destinations = appState.topLevelDestinations,
                    destinationsWithUnreadResources = setOf(TopLevelDestination.PROFILE),
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                )
            }
        },
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            if (appState.shouldShowNavRail) {
                JetpackNavRail(
                    destinations = appState.topLevelDestinations,
                    destinationsWithUnreadResources = unreadDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier.safeDrawingPadding(),
                )
            }

            Column(Modifier.fillMaxSize()) {
                // Show the top app bar on top level destinations.
                val destination = appState.currentTopLevelDestination
                if (destination != null) {
                    JetpackTopAppBar(
                        titleRes = destination.titleTextId,
                        navigationIcon = Icons.Default.Search,
                        navigationIconContentDescription = stringResource(id = R.string.search),
                        actionIcon = Icons.Default.MoreVert,
                        actionIconContentDescription = stringResource(id = R.string.more),
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        onActionClick = { },
                        onNavigationClick = { },
                    )
                }
                JetpackNavHost(
                    appState = appState,
                    onShowLoadingDialog = { shouldShowLoadingDialog = it },
                    onShowSnackbar = { message, action ->
                        snackBarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short,
                        ) == SnackbarResult.ActionPerformed
                    },
                )
            }
        }

        if (shouldShowLoadingDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "Loading ... ") },
                text = { Text(text = "Please Wait") },
                confirmButton = { },
            )
        }
    }
}
