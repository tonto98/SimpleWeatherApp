package com.example.weather_api_application.forecast.presenter

interface ForecastPresenter {
    fun loadCityForecast(city: String)
    fun onDaySelected(day: Int)
}