package com.example.weatherforecast.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "city_label")
    @NonNull
    val cityLabel:String,
    val country:String,
)
