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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.atick.compose.R
import dev.atick.compose.data.home.UiPost
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.utils.StatefulComposable

@Composable
internal fun DetailsRoute(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    val detailsUiState by detailsViewModel.detailsUiState.collectAsStateWithLifecycle()
    StatefulComposable(state = detailsUiState, onShowSnackbar = onShowSnackbar) {
        DetailsScreen(post = detailsUiState.data, onBackClick = onBackClick)
    }
}

@Composable
private fun DetailsScreen(
    post: UiPost?,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        DetailsToolbar(onBackClick = onBackClick)
        post?.let {
            AsyncImage(
                model = post.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun DetailsToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        JetpackButton(onClick = onBackClick) {
            Text(text = "Done")
        }
    }
}
