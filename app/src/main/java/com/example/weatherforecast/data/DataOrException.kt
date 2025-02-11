package com.example.weatherforecast.data

data class DataOrException<T, E: Exception> (
    var data: T?=null,
    var loading: Boolean? = null,
    var e: E?= null,
    var success: Boolean? =null
)