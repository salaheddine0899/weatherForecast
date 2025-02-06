package com.example.weatherforecast.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.weatherforecast.screens.topbars.BackTopBar

@Composable
fun CustomScaffold(title: String, navController: NavController?, container: @Composable ()-> Unit){
    Scaffold(topBar = {
        BackTopBar(title = title) { navController?.popBackStack() }
    }) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            container()
        }
    }
}