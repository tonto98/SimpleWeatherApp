package com.example.weather_api_application.data.response


import com.google.gson.annotations.SerializedName
import android.support.annotation.Keep

@Keep
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)