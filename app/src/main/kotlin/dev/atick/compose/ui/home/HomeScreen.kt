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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.compose.data.home.HomeScreenData
import dev.atick.core.ui.utils.StatefulComposable

/**
 * Composable function that represents the home screen.
 *
 * @param homeViewModel The view model for the home screen.
 */
@Composable
internal fun HomeRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = homeState,
        onShowSnackbar = onShowSnackbar,
    ) { homeScreenData ->
        HomeScreen(homeScreenData)
    }
}

/**
 * Composable function that displays the home screen.
 *
 * @param homeScreenData The data to be displayed on the home screen.
 */
@Composable
private fun HomeScreen(
    homeScreenData: HomeScreenData,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = homeScreenData.hello,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
