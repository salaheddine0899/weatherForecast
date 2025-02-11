package com.example.weatherforecast.model

data class Weather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDetails>,
    val message: Double
)