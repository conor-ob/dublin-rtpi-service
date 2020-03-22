package io.rtpi.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.Duration
import java.time.format.DateTimeParseException

class DurationTypeAdapter : JsonDeserializer<Duration> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Duration {
        try {
            if (typeOfT != null && typeOfT == Duration::class.java && json != null) {
                return Duration.parse(json.asJsonPrimitive.asString)
            }
        } catch (e : DateTimeParseException) {
            throw JsonParseException(e)
        }
        throw IllegalArgumentException("unknown type: $typeOfT")
    }
}
