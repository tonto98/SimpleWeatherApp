package com.example.weather_api_application.data.response


import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentWeatherResponse(
        val coord: Coord,
        val weather: List<Weather>,
        val base: String,
        @SerializedName("main")
        val mainWeatherInfo: MainWeatherInfo,
        val visibility: Int,
        val wind: Wind,
        val clouds: Clouds,
        val dt: Long,
        val sys: Sys,
        val timezone: Int,
        val id: Int,
        val name: String,
        val cod: Int,
        val dt_txt: String
)