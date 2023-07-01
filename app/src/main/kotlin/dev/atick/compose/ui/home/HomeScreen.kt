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

package dev.atick.compose.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.R
import dev.atick.compose.ui.home.components.HomeContent
import dev.atick.core.ui.components.TopBar
import kotlinx.coroutines.launch

/**
 * Composable function that represents the home screen.
 *
 * @param homeViewModel The view model for the home screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    homeUiState.toastMessage?.let {
        val errorMessage = it.asString()
        LaunchedEffect(homeUiState) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    snackbarHost.showSnackbar(errorMessage)
                    homeViewModel.clearError()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.home),
                onSearchClick = {},
                onMenuClick = {},
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            item = homeUiState.item,
            loading = homeUiState.loading,
            onButtonClick = { homeViewModel.getItem() },
        )
    }
}
