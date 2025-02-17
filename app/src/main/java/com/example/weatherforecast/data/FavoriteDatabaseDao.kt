package com.example.weatherforecast.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.entity.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDatabaseDao {
    @Query("select * from favorites")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("select * from favorites where city_label=:city")
    suspend fun getFavorite(city: String): Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(favorite: Favorite)

    @Query("delete from favorites")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}