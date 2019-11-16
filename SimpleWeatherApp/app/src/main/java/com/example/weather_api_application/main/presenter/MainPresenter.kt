package com.example.weather_api_application.main.presenter

interface MainPresenter {
    fun onWeatherApiRequestByLocation(lat: Double, lon: Double)
    fun onWeatherRequestByCity(city: String)
}