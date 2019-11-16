package com.example.weather_api_application.data.response


import com.google.gson.annotations.SerializedName
import android.support.annotation.Keep

@Keep
data class Coord(
    val lon: Double,
    val lat: Double
)