package com.example.weatherforecast.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherforecast.screens.topbars.BackTopBar

@Composable
fun CustomScaffold(title: String, goBack:()-> Unit, container: @Composable ()-> Unit){
    Scaffold(topBar = {
        BackTopBar(title = title) { goBack()}
    }) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            container()
        }
    }
}