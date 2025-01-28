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

package dev.atick.compose.data.settings

import dev.atick.storage.preferences.models.DarkThemeConfig
import dev.atick.storage.preferences.models.ThemeBrand

/**
 * Data class representing editable user settings related to themes and appearance.
 *
 * @property brand The selected brand for the theme.
 * @property useDynamicColor Indicates whether dynamic colors are enabled.
 * @property darkThemeConfig Configuration for the dark theme.
 * @constructor Creates a [UserEditableSettings] instance with optional parameters.
 */
data class UserEditableSettings(
    val brand: ThemeBrand = ThemeBrand.DEFAULT,
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
)
