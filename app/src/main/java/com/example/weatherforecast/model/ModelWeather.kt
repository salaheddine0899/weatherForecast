package com.example.weatherforecast.model

data class ModelWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Double
)