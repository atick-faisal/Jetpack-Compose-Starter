/*
 * Copyright 2025 Atick Faisal
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

package dev.atick.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

/**
 * Composable function that represents the floating action button in the Jetpack application.
 *
 * @param icon The icon to be displayed on the floating action button.
 * @param text The text to be displayed on the floating action button.
 * @param onClick Callback when the floating action button is clicked.
 * @param modifier Modifier to be applied to the floating action button.
 */
@Composable
fun JetpackExtendedFab(
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(icon, stringResource(text)) },
        text = { Text(text = stringResource(text)) },
        modifier = modifier,
    )
}
