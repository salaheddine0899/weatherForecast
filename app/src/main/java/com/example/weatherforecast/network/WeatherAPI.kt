package com.example.weatherforecast.network

import com.example.weatherforecast.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherAPI {
    @GET("data/2.5/forecast/daily")
    suspend fun getWeather(@Query("q") city: String,@Query("appid") appid: String, @Query("units")
    units: String): Weather
}