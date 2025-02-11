package com.example.weatherforecast.repositories

import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.network.WeatherAPI
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
) {
    private val dataOrException = DataOrException<Weather, Exception>()
    suspend fun getWeather(city: String, appid: String, units: String,): DataOrException<Weather, Exception>{
        dataOrException.loading = true
        try {
            dataOrException.data = api.getWeather(city = city, appid = appid, units = units)
            delay(2000)
            dataOrException.success = true
        }catch (exception: Exception){
            dataOrException.e = exception
            dataOrException.success =false
        }finally {
            dataOrException.loading = false
        }
        return dataOrException
    }
}