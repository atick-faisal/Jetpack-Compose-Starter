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

package dev.atick.compose.navigation.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.atick.compose.ui.details.DetailsRoute

internal const val postIdArg = "postId"

fun NavController.navigateToDetailsScreen(postId: Int) {
    navigate("details/$postId") { launchSingleTop = true }
}

fun NavGraphBuilder.detailsScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(
        route = "details/{$postIdArg}",
        arguments = listOf(
            navArgument(postIdArg) { type = NavType.IntType },
        ),
    ) {
        DetailsRoute(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
