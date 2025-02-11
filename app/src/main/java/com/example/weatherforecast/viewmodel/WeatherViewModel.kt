package com.example.weatherforecast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.BuildConfig.API_KEY
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val repository: WeatherRepository): ViewModel() {
    suspend fun reloadWeather(city: String, units: String): DataOrException<Weather, Exception>{
            return repository.getWeather(city = city, appid = API_KEY,
                units = units)
    }
}