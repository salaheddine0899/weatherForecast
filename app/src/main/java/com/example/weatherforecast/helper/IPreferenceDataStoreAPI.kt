package com.example.weatherforecast.helper

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface IPreferenceDataStoreAPI {
    suspend fun <T> getEncryptedPreference(key: Preferences.Key<String>, defaultValue: T,clazz:Class<T>): Flow<T>
    suspend fun <T> getEncryptedFirstPreference(key: Preferences.Key<String>,defaultValue: T,clazz: Class<T>):T
    suspend fun <T> putEncryptedPreference(key: Preferences.Key<String>,value:T)
    suspend fun <T> getPreference(key: Preferences.Key<String>, defaultValue: T,clazz:Class<T>): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<String>,defaultValue: T,clazz: Class<T>):T
    suspend fun <T> putPreference(key: Preferences.Key<String>,value:T)
    suspend fun removePreference(key: Preferences.Key<String>)
    suspend fun clearAllPreference()
}