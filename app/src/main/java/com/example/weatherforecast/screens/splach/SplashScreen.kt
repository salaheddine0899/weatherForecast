package com.example.weatherforecast.screens.splach

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherforecast.R
import com.example.weatherforecast.navigation.WeatherScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController?){
    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(
            durationMillis = 800,
            easing = {
                OvershootInterpolator(8f)
                    .getInterpolation(it)
            }
        ))
        delay(2000L)
        navController?.navigate(route = WeatherScreens.HomeScreen.path)
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(shape = CircleShape, border = BorderStroke(width = 2.dp, color = Color.LightGray),
            modifier = Modifier.width(350.dp).height(350.dp).scale(scale.value),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            onClick = {
            }
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = R.drawable.weather_cloud_svgrepo_com),
                    contentDescription = "", modifier = Modifier.fillMaxHeight(0.5f))
                Text(text = "Find the sun", fontSize = 30.sp)
            }
        }
    }
}