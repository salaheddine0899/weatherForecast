package com.example.weatherforecast.utils.adapters

import com.example.weatherforecast.enums.TemperatureUnitEnum
import com.google.gson.*
import java.lang.reflect.Type

class TemperatureUnitEnumTypeAdapter : JsonSerializer<TemperatureUnitEnum>, JsonDeserializer<TemperatureUnitEnum> {
    override fun serialize(src: TemperatureUnitEnum, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.unit)  // Serialize enum as lowercase
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TemperatureUnitEnum? {
        try {
            for( i in TemperatureUnitEnum.entries){
                if(i.unit == json.asString)
                    return i
            }
            return null
        } catch (e: IllegalArgumentException) {
            throw JsonParseException("Invalid value for temperature status: ${json.asString}")
        }
    }
}
