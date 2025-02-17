package com.example.weatherforecast.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object GlobalConstants {
    const val BASE_URL = "https://api.openweathermap.org"
    const val AES_GCM_NO_PADDING = "AES/GCM/NoPadding"

    const val HMAC_SHA256 = "HmacSHA256"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PreferenceDataStore")
}