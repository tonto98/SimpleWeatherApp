package com.example.weather_api_application.main

import com.example.weather_api_application.data.response.CurrentWeatherResponse

interface MainView {
    fun onWeatherApiResponse(currentWeatherResponse: CurrentWeatherResponse)
    fun showLoading()
    fun hideLoading()
}