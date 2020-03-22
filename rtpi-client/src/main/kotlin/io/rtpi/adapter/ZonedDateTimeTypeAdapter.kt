package io.rtpi.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

class ZonedDateTimeTypeAdapter : JsonDeserializer<ZonedDateTime> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ZonedDateTime {
        try {
            if (typeOfT != null && typeOfT == ZonedDateTime::class.java && json != null) {
                return ZonedDateTime.parse(json.asJsonPrimitive.asString)
            }
        } catch (e : DateTimeParseException) {
            throw JsonParseException(e)
        }
        throw IllegalArgumentException("unknown type: $typeOfT")
    }
}
