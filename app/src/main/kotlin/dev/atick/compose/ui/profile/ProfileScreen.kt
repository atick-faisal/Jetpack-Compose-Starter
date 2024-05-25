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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.atick.compose.R
import dev.atick.compose.data.profile.ProfileScreenData
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.utils.StatefulComposable

@Composable
internal fun ProfileRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by profileViewModel.profileUiState.collectAsState()

    StatefulComposable(
        state = profileState,
        onShowSnackbar = onShowSnackbar,
    ) { profileScreenData ->
        ProfileScreen(
            profileScreenData = profileScreenData,
            onSignOutClick = profileViewModel::signOut,
        )
    }
}

@Composable
private fun ProfileScreen(
    profileScreenData: ProfileScreenData,
    onSignOutClick: () -> Unit,

) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
        Spacer(modifier = Modifier.height(16.dp))
        JetpackButton(onClick = onSignOutClick) {
            Text(text = stringResource(id = R.string.sign_out))
        }
    }
}
