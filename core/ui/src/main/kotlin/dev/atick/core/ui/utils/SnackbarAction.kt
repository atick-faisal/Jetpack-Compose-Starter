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

package dev.atick.core.ui.utils

import androidx.annotation.StringRes
import dev.atick.core.ui.R

/**
 * Enum class representing different actions that can be taken on a Snackbar.
 * @param actionText The text to display for the action.
 */
enum class SnackbarAction(@StringRes val actionText: Int) {
    /**
     * No action.
     */
    NONE(R.string.empty),

    /**
     * Report action.
     */
    REPORT(R.string.report),

    /**
     * Undo action.
     */
    UNDO(R.string.undo),
}
