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

package dev.atick.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Title text for the top bar.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param title Title of the top bar.
 * @param color Color of the title text.
 */
@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier.then(Modifier.fillMaxHeight()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                )
                .width(8.dp)
                .height(32.dp)
                .background(color)
                .padding(end = 16.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * Top bar for the app.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param title Title of the top bar.
 * @param onSearchClick Callback when search button is clicked.
 * @param onRefreshClick Callback when refresh button is clicked.
 * @param onMenuClick Callback when menu button is clicked.
 * @param onExitClick Callback when exit button is clicked.
 * @param onLogoutClick Callback when logout button is clicked.
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    onSearchClick: (() -> Unit)? = null,
    onRefreshClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    onExitClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null,
) {
    return Card(
        modifier = modifier
            .then(Modifier.fillMaxWidth()),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TitleText(title = title)

            Row {
                onSearchClick?.let { onSearchClick ->
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    }
                }

                onRefreshClick?.let { onRefreshClick ->
                    IconButton(onClick = onRefreshClick) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                        )
                    }
                }

                onMenuClick?.let { onMenuClick ->
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                        )
                    }
                }

                onLogoutClick?.let { onLogoutClick ->
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                        )
                    }
                }

                onExitClick?.let { onExitClick ->
                    IconButton(onClick = onExitClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit",
                        )
                    }
                }
            }
        }
    }
}
