package com.example.weatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherforecast.screens.about.AboutScreen
import com.example.weatherforecast.screens.favorite.FavoriteCitiesScreen
import com.example.weatherforecast.screens.home.HomeScreen
import com.example.weatherforecast.screens.search.SearchScreen
import com.example.weatherforecast.screens.settings.SettingsScreen
import com.example.weatherforecast.screens.splach.SplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = WeatherScreens.SplashScreen.path){
        composable(route = WeatherScreens.HomeScreen.path+"/{city}",
            arguments = listOf(
                navArgument(name = "city"){
                    type = NavType.StringType
                }
            )
        ) { navBack-> navBack.arguments?.getString("city").let { city ->
                HomeScreen(navController = navController, city = city?:"Casablanca")
            }
        }
        composable(route = WeatherScreens.SplashScreen.path) {
            SplashScreen(navController = navController)
        }
        composable(route = WeatherScreens.AboutScreen.path) {
            AboutScreen(navController = navController)
        }
        composable(route = WeatherScreens.FavoriteCitiesScreen.path) {
            FavoriteCitiesScreen(navController = navController)
        }
        composable(route = WeatherScreens.SearchScreen.path) {
            SearchScreen(navController = navController)
        }
        composable(route = WeatherScreens.SettingsScreen.path) {
            SettingsScreen(navController = navController)
        }
    }
}