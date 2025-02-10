package com.example.weatherforecast.navigation

enum class WeatherScreens(val path: String) {
    HomeScreen(path = "home-screen"),
    SplashScreen(path = "splash-screen"),
    AboutScreen(path = "about-screen"),
    FavoriteCitiesScreen(path = "favorite-cities-screen"),
    SearchScreen(path = "search-screen"),
    SettingsScreen(path = "settings-screen");
    companion object{
        fun fromRoute(route: String?): WeatherScreens =
            when(route?.substringBefore("/")){
                HomeScreen.path -> HomeScreen
                SplashScreen.path -> SplashScreen
                AboutScreen.path -> AboutScreen
                FavoriteCitiesScreen.path -> AboutScreen
                SearchScreen.path -> SearchScreen
                SettingsScreen.path -> SettingsScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized.")

            }
    }
}