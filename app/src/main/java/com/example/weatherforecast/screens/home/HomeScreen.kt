package com.example.weatherforecast.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.utils.formatDate
import com.example.weatherforecast.viewmodel.WeatherViewModel
import java.time.LocalDate


@Composable
fun HomeScreen(
    navController: NavController?,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val weatherData = produceState<DataOrException<Weather, Exception>>(initialValue = DataOrException(loading = true)){
        value = viewModel.reloadWeather(city = "Casablanca", units = "metric")
    }.value

    LaunchedEffect(weatherData) {
        Log.d("TAG", "HomeScreen: ${weatherData.loading}")
    }
    if(weatherData.loading == true){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }
    else if (weatherData.success == true) {
        Scaffold(topBar = {
            HomeTopBar(
                title = weatherData.data?.city?.name+", "+ weatherData.data?.city?.country ,
                navController = navController
            )
        }) { innerPadding ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = formatDate(LocalDate.now()), fontWeight = FontWeight.Bold)
                    }
                }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    title: String,
    navController: NavController?
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(20.dp),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(horizontalArrangement = Arrangement.Start) {
                                Text(text = title, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(imageVector = Icons.Default.Search,
                                    contentDescription = "more vert",
                                    modifier = Modifier.clickable {
                                        navController?.navigate(
                                            route = WeatherScreens.SearchScreen.path
                                        )
                                    })
                                Spacer(modifier = Modifier.width(15.dp))
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "more vert"
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}