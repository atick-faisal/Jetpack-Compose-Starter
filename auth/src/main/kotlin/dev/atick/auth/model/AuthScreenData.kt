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

package dev.atick.auth.model

import android.content.IntentSender
import dev.atick.core.ui.utils.TextFiledData

/**
 * Data class representing the input data for an authentication screen.
 *
 * @param name The data for the user's name input field.
 * @param email The data for the user's email input field.
 * @param password The data for the user's password input field.
 * @param googleSignInIntent The [IntentSender] for initiating Google Sign-In, if available.
 */
data class AuthScreenData(
    val name: TextFiledData = TextFiledData(String()),
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
    val googleSignInIntent: IntentSender? = null,
)
