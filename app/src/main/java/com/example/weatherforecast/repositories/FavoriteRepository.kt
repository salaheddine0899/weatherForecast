package com.example.weatherforecast.repositories

import com.example.weatherforecast.data.FavoriteDatabaseDao
import com.example.weatherforecast.entity.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoriteDatabaseDao: FavoriteDatabaseDao) {
    suspend fun addFavorite(favorite: Favorite) = favoriteDatabaseDao.save(favorite)
    suspend fun deleteFavorite(favorite: Favorite) = favoriteDatabaseDao.deleteFavorite(favorite)
    fun getAllFavorites(): Flow<List<Favorite>> = favoriteDatabaseDao.getAllFavorites()
    suspend fun getFavorite(city: String) = favoriteDatabaseDao.getFavorite(city)
}