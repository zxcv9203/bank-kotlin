package org.example.cosumer.common.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object JsonUtils {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun <T> encodeToJson(v: T, serializer: KSerializer<T>): String {
        return json.encodeToString(serializer, v)
    }

    fun <T> decodeFromJson(v: String, serializer: KSerializer<T>): T {
        return json.decodeFromString(serializer, v)
    }
}