package com.example.weatherforecast.screens.topbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(title: String, goBack:()-> Unit){
    TopAppBar(
        title = { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
        {  Text(text = title, fontWeight = FontWeight.Bold) } },
        navigationIcon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "go back",
            modifier = Modifier.clickable{goBack()}) }
    )
}