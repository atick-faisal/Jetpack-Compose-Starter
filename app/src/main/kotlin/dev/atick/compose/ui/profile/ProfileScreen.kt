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

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.compose.data.profile.ProfileData
import dev.atick.core.ui.utils.StatefulComposable

@Composable
internal fun ProfileRoute(
    onShowLoadingDialog: (Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by viewModel.profileUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = profileState,
        onShowLoadingDialog = onShowLoadingDialog,
        onShowSnackbar = onShowSnackbar,
    ) { profileData ->
        ProfileScreen(profileData)
    }
}

@Preview
@Composable
private fun ProfileScreen(profileData: ProfileData = ProfileData()) {
    Text(text = profileData.name)
}
