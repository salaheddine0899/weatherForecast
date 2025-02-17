package com.example.weatherforecast.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.viewmodel.WeatherViewModel
import com.example.weatherforecast.widgets.CustomScaffold

@Composable
fun SettingsScreen(navController: NavController?,
                   viewModel: WeatherViewModel= hiltViewModel()
){
    val unitState by remember { viewModel.unitState}
    val unit = viewModel.unit.value
    if(unitState == WeatherViewModel.UnitState.Success(viewModel.unit.value))
    CustomScaffold(title = "Settings", navController = navController) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center) {
            Column (modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Change Units of Measurement", style = MaterialTheme.typography.headlineSmall)
                Card(modifier = Modifier.width(180.dp).height(50.dp), colors = CardDefaults.cardColors(containerColor = Color.Magenta,),
                    shape = RectangleShape) {
                    Row(modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "${unit.label} ${unit.symbol}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray,
                    contentColor = Color.White)) {
                    Text(text = "Save")
                }
            }
        }
    }
    else{
        CircularProgressIndicator()
    }
}