package com.example.weatherforecast.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.R
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.entity.Favorite
import com.example.weatherforecast.enums.TemperatureUnitEnum
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherDetails
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.ui.theme.YellowSun
import com.example.weatherforecast.utils.formatDate
import com.example.weatherforecast.utils.formatDecimals
import com.example.weatherforecast.utils.formatTimestampToTime
import com.example.weatherforecast.utils.getDayFromDate
import com.example.weatherforecast.viewmodel.FavoriteViewModel
import com.example.weatherforecast.viewmodel.WeatherViewModel
import com.example.weatherforecast.widgets.CustomCircularProgress
import com.example.weatherforecast.widgets.CustomImage


@Composable
fun HomeScreen(
    navController: NavController?,
    viewModel: WeatherViewModel = hiltViewModel(),
    city: String = "Casablanca"
) {


    if(viewModel.unitState.value ==  WeatherViewModel.UnitState.Success(
            viewModel.unit.value)) {
        val weatherData =
            produceState<DataOrException<Weather, Exception>>(initialValue = DataOrException(loading = true)) {
                value = viewModel.reloadWeather(city = city)
            }.value

        val showDialog = remember { mutableStateOf(false) }
        if (weatherData.loading == true) {
            CustomCircularProgress()
        } else if (weatherData.success == true) {
            if (showDialog.value) {
                ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
            }
            Scaffold(topBar = {
                HomeTopBar(
                    navController = navController,
                    showDialog = showDialog,
                    weatherData = weatherData
                )
            }) { innerPadding ->
                MainContent(innerPadding, weather = weatherData.data)
            }
        }
    }else
        CustomCircularProgress()


}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = showDialog.value,  // Use showDialog directly
            onDismissRequest = { showDialog.value = false },
            modifier = Modifier
                .width(140.dp)
                .background(color = Color.White)
        ) {
            Column {
                DropDownItem(
                    title = "Favorite",
                    icon = R.drawable.star,
                    navController = navController,
                    route = WeatherScreens.FavoriteCitiesScreen.path,
                    showDialog = showDialog
                )
                DropDownItem(
                    title = "About",
                    icon = R.drawable.info_outline,
                    navController = navController,
                    route = WeatherScreens.AboutScreen.path,
                    showDialog = showDialog
                )
                DropDownItem(
                    title = "Settings",
                    icon = R.drawable.settings,
                    navController = navController,
                    route = WeatherScreens.SettingsScreen.path,
                    showDialog = showDialog
                )
            }
        }
    }
}

@Composable
private fun DropDownItem(
    title: String,
    navController: NavController?,
    icon: Int,
    route: String,
    showDialog: MutableState<Boolean>
) {
    DropdownMenuItem(
        onClick = {
            navController?.navigate(route)
            showDialog.value = false // Close the dropdown when an item is clicked
        },
        text = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "drop down item"
                )
                Text(text = title)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
private fun MainContent(
    innerPadding: PaddingValues,
    weather: Weather?,
    viewModel: WeatherViewModel= hiltViewModel()
) {
    val weatherItem = weather!!.list[0]
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formatDate(weatherItem.dt), fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .size(200.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC400))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement =
                    Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WeatherStateImage(imageUrl = imageUrl)
                    Text(
                        text = formatDecimals(weatherItem.temp.day) + viewModel.unit.value.symbol,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = weatherItem.weather[0].main, fontStyle = FontStyle.Italic)
                }
            }
            HumidityWindPressureRow(weatherItem = weatherItem, unit = viewModel.unit.value)
            HorizontalDivider(thickness = 1.dp)
            SunSetAndRiseRow(weatherItem = weatherItem)
            WeatherWeek(weather = weather)
        }
    }
}

@Composable
fun WeatherWeek(weather: Weather) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "This Week",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
        LazyColumn(modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize()) {
            items(items = weather.list) { weatherItem ->
                val imageUrl =
                    "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
                Card(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .clip(
                            shape =
                            RoundedCornerShape(
                                bottomEnd = 30.dp,
                                topStart = 30.dp,
                                bottomStart = 30.dp
                            ),
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = getDayFromDate(weatherItem.sunset))
                        WeatherStateImage(imageUrl = imageUrl)
                        Card(
                            modifier = Modifier.padding(5.dp), shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = YellowSun)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Text(text = weatherItem.weather[0].main)
                            }
                        }
                        Row {
                            Text(
                                text = "${formatDecimals(weatherItem.temp.max)}°",
                                style = MaterialTheme
                                    .typography.titleMedium,
                                color = Color.Blue,
                            )
                            Text(
                                text = "${formatDecimals(weatherItem.temp.min)}°",
                                style = MaterialTheme
                                    .typography.titleMedium,
                                color = Color.LightGray,
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun SunSetAndRiseRow(weatherItem: WeatherDetails) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "sunrise"
            )
            Text(text = formatTimestampToTime(weatherItem.sunrise))
        }
        Row {
            Text(text = formatTimestampToTime(weatherItem.sunset))
        }
    }

}

@Composable
private fun HumidityWindPressureRow(weatherItem: WeatherDetails, unit: TemperatureUnitEnum) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RowItem(text = weatherItem.humidity.toString() + "%", icon = R.drawable.humidity)
        RowItem(text = weatherItem.pressure.toString() + " psi", icon = R.drawable.barometer)
        RowItem(text = formatDecimals(weatherItem.gust) + if(unit == TemperatureUnitEnum.FAHRENHEIT)
            "mph" else "m/s", icon = R.drawable.wind)
    }
}

@Composable
private fun RowItem(text: String, icon: Int) {
    Row {
        Icon(
            painter = painterResource(id = icon), contentDescription = "humidity",
            modifier = Modifier.padding(end = 2.dp)
        )
        Text(text = text)
    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    CustomImage(data = imageUrl, modifier = Modifier.size(80.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    navController: NavController?,
    showDialog: MutableState<Boolean>,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    weatherData: DataOrException<Weather, Exception>
) {
    favoriteViewModel.isCityFavorite(weatherData.data!!.city.name)
    Log.d("isFav", favoriteViewModel.isFavorite.value.toString())
    TopAppBar(
        title = { Text(text = weatherData.data?.city?.name +
                ", " + weatherData.data?.city?.country) },
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            if(favoriteViewModel.isFavorite.value)
                                IconButton(onClick = {
                                    favoriteViewModel.deleteFavorite(Favorite(cityLabel = weatherData.data!!.city.name,
                                        country = weatherData.data!!.city.country))
                                    favoriteViewModel.isFavorite.value = false
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.star),
                                        contentDescription = "favorite city"
                                    )
                                }
                            else
                                IconButton(onClick = {
                                    favoriteViewModel.addFavorite(Favorite(cityLabel = weatherData.data!!.city.name,
                                        country = weatherData.data!!.city.country))
                                    favoriteViewModel.isFavorite.value = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_outline),
                                        contentDescription = "favorite city"
                                    )
                                }
                        }
                        Row {
                            Text(text = weatherData.data?.city?.name + ", "
                                    + weatherData.data?.city?.country, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Search,
                                contentDescription = "search icon",
                                modifier = Modifier.clickable {
                                    navController?.navigate(
                                        route = WeatherScreens.SearchScreen.path
                                    )
                                })
                            Spacer(modifier = Modifier.width(15.dp))
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more vert",
                                modifier = Modifier.clickable {
                                    showDialog.value = !showDialog.value
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}