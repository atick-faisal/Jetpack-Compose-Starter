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

package dev.atick.storage.preferences.utils

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import dev.atick.storage.preferences.model.DarkThemeConfig
import dev.atick.storage.preferences.model.ThemeBrand
import dev.atick.storage.preferences.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for serializing and deserializing [UserData] objects.
 */
object UserDataSerializer : Serializer<UserData> {

    /**
     * The default value of [UserData] to be used when deserialization fails.
     */
    override val defaultValue: UserData = UserData()

    /**
     * Reads a [UserData] object from the provided [InputStream].
     *
     * @param input The input stream to read data from.
     * @return The deserialized [UserData] object.
     * @throws CorruptionException if there's an issue with deserialization.
     */
    override suspend fun readFrom(input: InputStream): UserData {
        try {
            return Json.decodeFromString(
                UserData.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    /**
     * Writes a [UserData] object to the provided [OutputStream].
     *
     * @param t The [UserData] object to be serialized.
     * @param output The output stream to write data to.
     */
    override suspend fun writeTo(t: UserData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(UserData.serializer(), t)
                    .encodeToByteArray(),
            )
        }
    }
}

/**
 * Custom serializer for serializing and deserializing [DarkThemeConfig] enums.
 */
object DarkThemeConfigSerializer : KSerializer<DarkThemeConfig> {
    /**
     * The descriptor for the serialized form of [DarkThemeConfig].
     */
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DarkThemeConfig", PrimitiveKind.STRING)

    /**
     * Serializes the provided [value] of [DarkThemeConfig] enum to a string representation.
     *
     * @param encoder The encoder to write the serialized data to.
     * @param value The [DarkThemeConfig] value to be serialized.
     */
    override fun serialize(encoder: Encoder, value: DarkThemeConfig) {
        encoder.encodeString(value.name)
    }

    /**
     * Deserializes the string representation from the provided [decoder] and converts it to a [DarkThemeConfig] enum.
     *
     * @param decoder The decoder to read the serialized data from.
     * @return The deserialized [DarkThemeConfig] enum value.
     */
    override fun deserialize(decoder: Decoder): DarkThemeConfig {
        return DarkThemeConfig.valueOf(decoder.decodeString())
    }
}

/**
 * Custom serializer for serializing and deserializing [ThemeBrand] enums.
 */
object ThemeBrandSerializer : KSerializer<ThemeBrand> {
    /**
     * The descriptor for the serialized form of [ThemeBrand].
     */
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ThemeBrand", PrimitiveKind.STRING)

    /**
     * Serializes the provided [value] of [ThemeBrand] enum to a string representation.
     *
     * @param encoder The encoder to write the serialized data to.
     * @param value The [ThemeBrand] value to be serialized.
     */
    override fun serialize(encoder: Encoder, value: ThemeBrand) {
        encoder.encodeString(value.name)
    }

    /**
     * Deserializes the string representation from the provided [decoder] and converts it to a [ThemeBrand] enum.
     *
     * @param decoder The decoder to read the serialized data from.
     * @return The deserialized [ThemeBrand] enum value.
     */
    override fun deserialize(decoder: Decoder): ThemeBrand {
        return ThemeBrand.valueOf(decoder.decodeString())
    }
}
