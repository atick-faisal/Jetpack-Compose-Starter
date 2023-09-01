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

package dev.atick.core.utils

import android.net.Uri
import javax.inject.Inject

/**
 * Implementation of [StringDecoder] that uses Android's Uri.decode method for decoding strings.
 *
 * @constructor Creates a [UriDecoder] instance.
 */
class UriDecoder @Inject constructor() : StringDecoder {
    /**
     * Decodes an encoded string using Android's Uri.decode method.
     *
     * @param encodedString The string to be decoded.
     * @return The decoded string.
     */
    override fun decodeString(encodedString: String): String = Uri.decode(encodedString)
}
