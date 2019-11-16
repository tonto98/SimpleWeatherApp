package com.example.weather_api_application.data.response

import com.example.weather_api_application.data.response.CurrentWeatherResponse
import com.google.gson.annotations.SerializedName

data class ForecastWeatherReponse(
        @SerializedName("list")
        val forecastList: List<CurrentWeatherResponse>
)