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

package dev.atick.compose.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.components.JetpackTextFiled
import dev.atick.core.ui.utils.SnackbarAction
import dev.atick.core.ui.utils.StatefulComposable
import dev.atick.demo.R

@Composable
internal fun DetailsRoute(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction) -> Boolean,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    val detailsUiState by detailsViewModel.detailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        detailsViewModel.getJetpack()
    }

    StatefulComposable(
        state = detailsUiState,
        onShowSnackbar = onShowSnackbar,
    ) { data ->

        LaunchedEffect(data.navigateBack) {
            if (data.navigateBack.getContentIfNotHandled() == true) {
                onBackClick()
            }
        }

        DetailsScreen(
            name = data.name,
            price = data.price,
            onUpdateName = detailsViewModel::updateName,
            onUpdatePrice = detailsViewModel::updatePrice,
            onSaveClick = detailsViewModel::updateOrInsertJetpack,
            onBackClick = onBackClick,
        )
    }
}

@Composable
private fun DetailsScreen(
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
        DetailsToolbar(
            onSaveClick = onSaveClick,
            onBackClick = onBackClick,
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

@Composable
private fun DetailsToolbar(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        JetpackButton(onClick = onSaveClick) {
            Text(text = stringResource(R.string.save))
        }
    }
}
