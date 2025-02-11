package com.example.weatherforecast.di

import com.example.weatherforecast.network.WeatherAPI
import com.example.weatherforecast.repositories.WeatherRepository
import com.example.weatherforecast.utils.GlobalConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
}