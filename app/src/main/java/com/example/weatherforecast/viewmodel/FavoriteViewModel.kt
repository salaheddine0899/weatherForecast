package com.example.weatherforecast.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.entity.Favorite
import com.example.weatherforecast.repositories.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository): ViewModel() {
    private val _favoriteList = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteList = _favoriteList.asStateFlow()
    val isFavorite: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavorites().distinctUntilChanged()
                .collect{
                    listOfFavorites ->
                    if (listOfFavorites.isEmpty()){
                        Log.d("empty", "empty list")
                    }else{
                        _favoriteList.value = listOfFavorites
                    }
                }
        }
    }

    fun addFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.addFavorite(favorite)
        }
    }
    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }
    fun isCityFavorite(city: String){
        viewModelScope.launch {
            isFavorite.value = repository.getFavorite(city)!=null
            Log.d("TAG", repository.getFavorite(city).toString())
        }
    }
}