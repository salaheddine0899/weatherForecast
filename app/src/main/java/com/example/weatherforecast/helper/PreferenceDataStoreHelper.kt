package com.example.weatherforecast.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.weatherforecast.exception.DecryptionException
import com.example.weatherforecast.exception.EncryptionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject


class PreferenceDataStoreHelper @Inject constructor(private val dataStore: DataStore<Preferences>,private val aesCipherHelper: AESCipherHelper,private val toJsonSerializerHelper: ToJsonSerializerHelper): IPreferenceDataStoreAPI {

    override suspend fun <T> getPreference(key: Preferences.Key<String>, defaultValue: T,clazz:Class<T>):
            Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map { preferences->
        val result = preferences[key]
        if(result != null){
            toJsonSerializerHelper.fromJson(result,clazz) ?:defaultValue
        }
        else{
            defaultValue
        }
    }

    /* This returns the last saved value of the key. If we change the value,
        it wont effect the values produced by this function */
    override suspend fun <T> getFirstPreference(key: Preferences.Key<String>, defaultValue: T,clazz: Class<T>) :
            T {
        val result= dataStore.data.first()[key]
        return if(result != null) {
            toJsonSerializerHelper.fromJson(result,clazz)  ?: defaultValue
        }else{
            defaultValue
        }

    }

    // This Sets the value based on the value passed in value parameter.
    override suspend fun <T> putPreference(key: Preferences.Key<String>, value: T) {
        dataStore.edit {   preferences ->
            toJsonSerializerHelper.toJson(value)?.let{
                preferences[key] = it
            }
        }
    }


    override suspend fun <T> getEncryptedPreference(key: Preferences.Key<String>, defaultValue: T,clazz:Class<T>):
            Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map { preferences->
        val result = preferences[key]
        if(result != null){
            toJsonSerializerHelper.fromJson(aesCipherHelper.decryptData(result.toString()),clazz) ?:defaultValue
        }
        else{
            defaultValue
        }
    }

    /* This returns the last saved value of the key. If we change the value,
        it wont effect the values produced by this function */
    override suspend fun <T> getEncryptedFirstPreference(key: Preferences.Key<String>, defaultValue: T,clazz: Class<T>) :
            T {
        val result= dataStore.data.first()[key]
        return if(result != null) {
            try {
                toJsonSerializerHelper.fromJson(aesCipherHelper.decryptData(result),clazz)  ?: defaultValue
            }catch(ex : DecryptionException){
                return defaultValue
            }

        }else{
            defaultValue
        }

    }

    // This Sets the value based on the value passed in value parameter.
    override suspend fun <T> putEncryptedPreference(key: Preferences.Key<String>, value: T) {
        dataStore.edit {   preferences ->
            toJsonSerializerHelper.toJson(value)?.let{
                try{
                    preferences[key] = aesCipherHelper.encryptData(it)
                }catch (ex: EncryptionException){
                    ex.printStackTrace()
                }
            }
        }
    }

    // This Function removes the Key Value pair from the datastore, hereby removing it completely.
    override suspend fun removePreference(key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    // This function clears the entire Preference Datastore.
    override suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}