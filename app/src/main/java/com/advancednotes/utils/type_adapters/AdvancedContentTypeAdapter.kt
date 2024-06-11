package com.advancednotes.utils.type_adapters

import com.advancednotes.domain.models.AdvancedContentType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class AdvancedContentTypeAdapter : JsonSerializer<AdvancedContentType>,
    JsonDeserializer<AdvancedContentType> {

    override fun serialize(
        src: AdvancedContentType,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.value)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AdvancedContentType {
        val value = json.asString
        return AdvancedContentType.values().find { it.value == value } ?: AdvancedContentType.OTHER
    }
}