package com.example.weatherforecast.helper

import com.google.gson.Gson

class ToJsonSerializerHelper constructor(private val gson: Gson) {

    fun <T> toJson(obj: T?): String? {
        if(obj != null)
            return gson.toJson(obj)
        else
            return null
    }

    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if(json != null)
        return gson.fromJson(json, clazz)
        else
            return null
    }
}