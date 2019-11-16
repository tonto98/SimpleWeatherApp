package com.example.weather_api_application.data.response


import com.google.gson.annotations.SerializedName
import android.support.annotation.Keep

@Keep
data class Sys(
    val type: Int,
    val id: Int,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)