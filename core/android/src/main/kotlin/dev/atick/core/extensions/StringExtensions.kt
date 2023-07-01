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

package dev.atick.core.extensions

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Checks if the string is a valid email address.
 *
 * @return `true` if the string is a valid email address, `false` otherwise.
 */
fun String?.isEmailValid(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Checks if the string is a valid password based on the specified criteria.
 *
 * @return `true` if the string is a valid password, `false` otherwise.
 */
fun String?.isPasswordValid(): Boolean {
    val passwordRegex = "^(?=.*\\d)(?=.*[a-z]).{8,20}$"
    val pattern: Pattern = Pattern.compile(passwordRegex)
    val matcher: Matcher = pattern.matcher(this ?: "")
    return matcher.matches()
}
