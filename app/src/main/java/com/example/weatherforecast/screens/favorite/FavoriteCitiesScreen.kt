package com.example.weatherforecast.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.viewmodel.FavoriteViewModel
import com.example.weatherforecast.widgets.CustomScaffold
@Composable
fun FavoriteCitiesScreen(navController: NavController?, favoriteViewModel: FavoriteViewModel = hiltViewModel(), ){
    val cities = favoriteViewModel.favoriteList.collectAsState().value
    CustomScaffold(title = "Favorite Cities", navController = navController) {
        LazyColumn {
            items(items = cities){ city ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.Cyan),
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 15.dp).
                    clip(RoundedCornerShape(bottomEnd = 33.dp, topStart = 33.dp,
                        bottomStart = 33.dp),),
                    onClick = {navController?.navigate(
                        route = "${WeatherScreens.HomeScreen.path}/${city.cityLabel}")}
                    ) {
                    Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.
                        SpaceEvenly) {
                            Text(text = city.cityLabel, fontWeight = FontWeight.Bold)
                            Card(colors = CardDefaults.cardColors(containerColor = Color.Green),) {
                                Text(text = city.country)
                            }
                            IconButton( onClick = {
                                favoriteViewModel.deleteFavorite(city)
                            }) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete city")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}