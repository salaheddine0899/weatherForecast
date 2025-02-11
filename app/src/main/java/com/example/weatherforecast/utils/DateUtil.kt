package com.example.weatherforecast.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(date: LocalDate): String{
    val formatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)
    val formattedDate = date.format(formatter)
    return formattedDate
}