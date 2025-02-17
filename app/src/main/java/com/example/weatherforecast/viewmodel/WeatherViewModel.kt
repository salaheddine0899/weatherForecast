package com.example.weatherforecast.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.BuildConfig.API_KEY
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.data.dataStore.UnitDataStore
import com.example.weatherforecast.enums.TemperatureUnitEnum
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository,
    private val dataStore: UnitDataStore): ViewModel() {
        private val _unit:MutableState<TemperatureUnitEnum> = mutableStateOf(TemperatureUnitEnum.CELSIUS)
        val unit: State<TemperatureUnitEnum> = _unit
        private val _unitState = mutableStateOf<UnitState>(UnitState.Idle)
        val unitState: State<UnitState> = _unitState

    init {
        viewModelScope.launch {
            dataStore.getUnit().collect {
                _unitState.value = UnitState.Loading
                delay(50)
                _unit.value = TemperatureUnitEnum.getByCode(it)
                _unitState.value = UnitState.Success(_unit.value)
                dataStore.putUnit(_unit.value.unit)
            }
        }
    }

    fun setUnit(unit: TemperatureUnitEnum){
        viewModelScope.launch {
            dataStore.putUnit(unit.unit)
        }
    }
    val weatherData:MutableState<DataOrException<Weather,Exception>> = mutableStateOf(DataOrException(loading = true))
    suspend fun reloadWeather(city: String="Casablanca"): DataOrException<Weather, Exception>{
        Log.d("test", unit.value.unit)
            return repository.getWeather(city = city, appid = API_KEY, units = unit.value.unit)
    }

    sealed class UnitState {
        object Idle : UnitState()
        object Loading : UnitState()
        data class Success(val unit: TemperatureUnitEnum) : UnitState()
    }
}