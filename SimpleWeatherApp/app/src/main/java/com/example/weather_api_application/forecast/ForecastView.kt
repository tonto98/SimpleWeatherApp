package com.example.weather_api_application.forecast

import com.example.weather_api_application.data.response.CurrentWeatherResponse

interface ForecastView {
    fun onForecastLoaded(forecastMap: MutableMap<String, MutableList<CurrentWeatherResponse>>)
    fun showForecastForSelectedDay(forecastList: MutableList<CurrentWeatherResponse>, day: Int)
}