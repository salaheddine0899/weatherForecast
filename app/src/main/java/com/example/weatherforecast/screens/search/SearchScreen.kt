package com.example.weatherforecast.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.viewmodel.WeatherViewModel
import com.example.weatherforecast.widgets.CustomCircularProgress
import com.example.weatherforecast.widgets.CustomScaffold

@Composable
fun SearchScreen(navController: NavController?, viewModel: WeatherViewModel= hiltViewModel()){

    val cityState = rememberSaveable{ mutableStateOf("") }
    val weatherData = viewModel.weatherData.value

    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(cityState.value) { cityState.value.isNotBlank() }

        CustomScaffold(
            title = "Search",
            navController = navController
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = cityState.value,
                        onValueChange = {cityState.value = it},
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Text,
                            imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions{
                            if(!valid) return@KeyboardActions
                            navController?.navigate(
                                route = "${WeatherScreens.HomeScreen.path}/${cityState.value}")
                            cityState.value=""
                            keyboardController?.hide()
                        },
                        singleLine = true,
                        maxLines = 1,
                    )
                }
                if(weatherData.loading == true)
                    CustomCircularProgress()
                else if(weatherData.success == true) {
                Text(text= weatherData.data!!.city.name)
                }
            }
        }
}