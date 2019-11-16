package com.example.weather_api_application.data.response


import com.google.gson.annotations.SerializedName
import android.support.annotation.Keep

@Keep
data class MainWeatherInfo(
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)