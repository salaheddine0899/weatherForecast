package com.example.weatherforecast.widgets

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import coil3.transform.Transformation
import com.example.weatherforecast.R

@Composable
fun CustomImage(modifier: Modifier = Modifier, data: String,
                transformation: Transformation = CircleCropTransformation()
){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .transformations(transformation)
            .listener(
                onSuccess = { it1, it2->
                },
                onError = { request, result -> Log.e("Coil", "Error loading image", result.throwable) }
            )
            .build(),
        imageLoader = ImageLoader(LocalContext.current),
        contentDescription = "Image from URL",
        modifier = modifier,
        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
        error = painterResource(id = R.drawable.ic_launcher_foreground)
    )
}