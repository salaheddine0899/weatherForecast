package com.example.weatherforecast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.weatherforecast.data.FavoriteDatabase
import com.example.weatherforecast.data.FavoriteDatabaseDao
import com.example.weatherforecast.data.dataStore.UnitDataStore
import com.example.weatherforecast.enums.TemperatureUnitEnum
import com.example.weatherforecast.helper.AESCipherHelper
import com.example.weatherforecast.helper.PreferenceDataStoreHelper
import com.example.weatherforecast.helper.ToJsonSerializerHelper
import com.example.weatherforecast.network.WeatherAPI
import com.example.weatherforecast.repositories.WeatherRepository
import com.example.weatherforecast.utils.GlobalConstants
import com.example.weatherforecast.utils.GlobalConstants.dataStore
import com.example.weatherforecast.utils.adapters.GsonToLocalTimeZoneDateAdapter
import com.example.weatherforecast.utils.adapters.TemperatureUnitEnumTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideWeatherAPI(): WeatherAPI =
        Retrofit.Builder().baseUrl(GlobalConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherAPI::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherAPI): WeatherRepository = WeatherRepository(api)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): FavoriteDatabase =
        Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorites_db")
            .fallbackToDestructiveMigration().build()
    @Provides
    @Singleton
    fun providesFavoritesDao(favoriteDatabase: FavoriteDatabase): FavoriteDatabaseDao =
        favoriteDatabase.favoriteDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context):DataStore<Preferences>{
        return context.dataStore
    }
    @Provides
    @Singleton
    fun providePreferenceDataStoreHelper(dataStore: DataStore<Preferences>, aesCipherHelper: AESCipherHelper, toJsonSerializerHelper: ToJsonSerializerHelper):
            PreferenceDataStoreHelper{
        return PreferenceDataStoreHelper(dataStore, aesCipherHelper, toJsonSerializerHelper)
    }
    @Provides
    @Singleton
    fun provideUnitDataStore( preferenceDataStoreHelper: PreferenceDataStoreHelper): UnitDataStore {
        return UnitDataStore(preferenceDataStoreHelper)
    }
    @Provides
    @Singleton
    fun getCipherHelper(): AESCipherHelper {
        return AESCipherHelper()
    }

    @Provides
    @Singleton
    fun getSerializerHelper(gson: Gson): ToJsonSerializerHelper {
        return ToJsonSerializerHelper(gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, GsonToLocalTimeZoneDateAdapter())
            .registerTypeAdapter(TemperatureUnitEnum::class.java, TemperatureUnitEnumTypeAdapter())
            .create()
    }
}