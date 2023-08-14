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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.atick.compose.data.home.HomeData
import dev.atick.core.ui.theme.JetpackTheme
import dev.atick.core.ui.utils.StatefulComposable

/**
 * Composable function that represents the home screen.
 *
 * @param homeViewModel The view model for the home screen.
 */
@Composable
internal fun HomeRoute(
    onShowLoadingDialog: (Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by homeViewModel.homeUiState.collectAsState()

    StatefulComposable(
        state = homeState,
        onShowLoadingDialog = onShowLoadingDialog,
        onShowSnackbar = onShowSnackbar,
    ) {
        HomeScreen()
    }
}

@Composable
private fun HomeScreen() {
    val homeItems by remember { mutableStateOf(List(50) { HomeData() }) }
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = scrollableState,
    ) {
        items(homeItems) { item ->
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Sailing,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                    )
                },
                headlineContent = { Text(text = item.name) },
                supportingContent = { Text(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.") },
                trailingContent = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    JetpackTheme {
        Surface {
            HomeScreen()
        }
    }
}
