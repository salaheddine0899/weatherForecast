package com.example.weatherforecast.data.dataStore

import com.example.weatherforecast.data.dataStore.PreferenceDataStoreConstants.UNIT
import com.example.weatherforecast.enums.TemperatureUnitEnum
import com.example.weatherforecast.helper.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitDataStore @Inject constructor(private val preferenceDataStore:
                                        PreferenceDataStoreHelper) {
    suspend fun putUnit(localUnit: String) {
        preferenceDataStore.putPreference(UNIT, localUnit)
    }

    suspend fun getUnit(): Flow<String> {
        return preferenceDataStore.getPreference(UNIT, TemperatureUnitEnum.CELSIUS.unit, String::class.java)
    }

    suspend fun removeUnit() {
        preferenceDataStore.removePreference(UNIT)
    }
}