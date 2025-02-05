package com.example.weatherforecast.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.weatherforecast.widgets.CustomScaffold

@Composable
fun SearchScreen(){
    val cityState = remember { mutableStateOf("Casablanca") }
    CustomScaffold(
        title = "Search",
        goBack = {}
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                OutlinedTextField(
                    value = cityState.value,
                    onValueChange = {cityState.value = it},
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}