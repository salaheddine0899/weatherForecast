package com.example.weatherforecast.enums

enum class TemperatureUnitEnum(val unit:String, val label:String,
    val symbol: String) {
    CELSIUS(unit = "metric", label = "Celsius", symbol="°C"),
    FAHRENHEIT(unit = "imperial", label = "Fahrenheit", symbol="°F");
    companion object {
        fun getByCode(code: String): TemperatureUnitEnum {
            return entries.find { it.unit == code } ?: CELSIUS
        }
    }
}