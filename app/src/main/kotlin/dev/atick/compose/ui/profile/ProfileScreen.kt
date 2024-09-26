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

package dev.atick.compose.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.atick.compose.R
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.components.JetpackOutlinedButton
import dev.atick.core.ui.utils.StatefulComposable

@Composable
internal fun ProfileRoute(
    onPurchaseClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by profileViewModel.profileUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = profileState,
        onShowSnackbar = onShowSnackbar,
    ) { profileScreenData ->
        ProfileScreen(
            profileScreenData = profileScreenData,
            onSignOutClick = profileViewModel::signOut,
            onPurchaseClick = onPurchaseClick,
        )
    }
}

@Composable
private fun ProfileScreen(
    profileScreenData: ProfileScreenData,
    onSignOutClick: () -> Unit,
    onPurchaseClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        AsyncImage(
            model = profileScreenData.profilePictureUri,
            contentDescription = stringResource(R.string.profile_picture),
            placeholder = painterResource(id = R.drawable.ic_avatar),
            fallback = painterResource(id = R.drawable.ic_avatar),
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = profileScreenData.name, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.weight(1f))
        JetpackOutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = onSignOutClick) {
            Text(text = stringResource(id = R.string.sign_out))
        }
        Spacer(modifier = Modifier.height(8.dp))
        JetpackButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onPurchaseClick,
            text = { Text(text = stringResource(id = R.string.purchase_premium)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
