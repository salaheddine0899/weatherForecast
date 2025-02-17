package com.example.weatherforecast.utils.adapters

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GsonToLocalTimeZoneDateAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val utcDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")  // Set UTC timezone for the UTC format
    }

    private val localDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getDefault()  // Use the system's local timezone
    }

    @Synchronized
    override fun serialize(date: Date, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        // Convert the local timezone date to UTC using the UTC date format
        val utcDateString = utcDateFormat.format(date)
        return JsonPrimitive(utcDateString)
    }

    @Synchronized
    override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): Date {
        return try {
            // Parse the UTC date string to a Date object and then convert to the local timezone
            val utcDate = utcDateFormat.parse(jsonElement.asString) ?: throw JsonParseException("Unable to parse date")
            // Format the UTC date into the local timezone format
            val localDateString = localDateFormat.format(utcDate)
            localDateFormat.parse(localDateString) ?: throw JsonParseException("Unable to parse date to local timezone")
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }
}
