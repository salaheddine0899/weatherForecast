package com.example.weatherforecast.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(timestamp: Long): String{
    return formatDateWithFormat(timestamp, "E, MMM d")
}

fun formatDateWithFormat(timestamp: Long, format: String): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
    val formattedDate = localDate.format(formatter)
    return formattedDate
}

fun formatTimestampToTime(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return localTime.format(formatter)
}

fun getDayFromDate(timestamp: Long): String {
    return formatDateWithFormat(timestamp = timestamp, format = "E")
}