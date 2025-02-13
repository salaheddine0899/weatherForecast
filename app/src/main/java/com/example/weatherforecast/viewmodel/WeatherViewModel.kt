package com.example.weatherforecast.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.BuildConfig.API_KEY
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    val weatherData:MutableState<DataOrException<Weather,Exception>> = mutableStateOf(DataOrException(loading = true))
    suspend fun reloadWeather(city: String="Casablanca", units: String="metric"): DataOrException<Weather, Exception>{
            return repository.getWeather(city = city, appid = API_KEY, units = units)
    }

     fun updateWeather(city: String="Casablanca", units: String="metric"){
         viewModelScope.launch {
            weatherData.value = repository.getWeather(city = city, appid = API_KEY, units = units)
         }
    }
}