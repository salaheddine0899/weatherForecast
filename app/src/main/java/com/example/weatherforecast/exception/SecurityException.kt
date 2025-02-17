package com.example.weatherforecast.exception

open class SecurityException(val code :Int, val tag:String, override val message:String) : Exception()