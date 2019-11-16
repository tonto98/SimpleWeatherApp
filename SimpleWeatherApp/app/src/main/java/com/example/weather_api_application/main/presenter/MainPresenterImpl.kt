package com.example.weather_api_application.main.presenter

import com.example.weather_api_application.data.WeatherApiService
import com.example.weather_api_application.main.MainView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainPresenterImpl(var mainView: MainView): MainPresenter {

    private val apiService = WeatherApiService()

    override fun onWeatherApiRequestByLocation(lat: Double, lon: Double) {

        GlobalScope.launch(Dispatchers.Main){
            val currentWeatherResponse = apiService.getCurrentWeatherByLocation(lat, lon).await()
            mainView.onWeatherApiResponse(currentWeatherResponse)
        }
    }

    override fun onWeatherRequestByCity(city: String) {
        GlobalScope.launch(Dispatchers.Main){
            val currentWeatherResponse = apiService.getCurrentWeatherByCity(city).await()
            mainView.onWeatherApiResponse(currentWeatherResponse)
        }
    }



}