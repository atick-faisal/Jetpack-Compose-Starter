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

package dev.atick.feature.home.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.core.ui.components.JetpackActionBar
import dev.atick.core.ui.components.JetpackTextFiled
import dev.atick.core.ui.utils.SnackbarAction
import dev.atick.core.ui.utils.StatefulComposable
import dev.atick.feature.home.R

@Composable
internal fun ItemRoute(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    val itemUiState by itemViewModel.itemUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        itemViewModel.getJetpack()
    }

    StatefulComposable(
        state = itemUiState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->

        LaunchedEffect(screenData.navigateBack) {
            if (screenData.navigateBack.getContentIfNotHandled() == true) {
                onBackClick()
            }
        }

        ItemScreen(
            name = screenData.jetpackName,
            price = screenData.jetpackPrice,
            onUpdateName = itemViewModel::updateName,
            onUpdatePrice = itemViewModel::updatePrice,
            onSaveClick = itemViewModel::updateOrInsertJetpack,
            onBackClick = onBackClick,
        )
    }
}

@Composable
private fun ItemScreen(
    name: String,
    price: Double,
    onUpdateName: (String) -> Unit,
    onUpdatePrice: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        JetpackActionBar(
            titleRes = R.string.jetpack,
            actionRes = R.string.save,
            onActionClick = onSaveClick,
            onNavigateBackClick = onBackClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        JetpackTextFiled(
            value = name,
            onValueChange = onUpdateName,
            label = { Text(text = stringResource(id = R.string.name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = stringResource(id = R.string.name),
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        JetpackTextFiled(
            value = price.toString(),
            onValueChange = onUpdatePrice,
            label = { Text(text = stringResource(id = R.string.price)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = stringResource(id = R.string.price),
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}