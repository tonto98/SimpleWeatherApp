package com.example.weather_api_application.data

import android.content.res.Resources
import com.example.weather_api_application.R
import com.example.weather_api_application.citydata.model.Cities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonResourceWrapper(val resources: Resources) {

    val jsonInput = resources.openRawResource(R.raw.city).bufferedReader().use {
        it.readText()
    }
    val gson = Gson()

    var cityList: List<Cities> = gson.fromJson(jsonInput, object: TypeToken<List<Cities>>() {}.type)
}