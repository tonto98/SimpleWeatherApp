package com.example.weather_api_application.forecast.presenter

import android.util.Log
import com.example.weather_api_application.data.WeatherApiService
import com.example.weather_api_application.data.response.CurrentWeatherResponse
import com.example.weather_api_application.forecast.ForecastView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForecastPresenterImpl(val forecastView: ForecastView): ForecastPresenter {

    private val apiService = WeatherApiService()
    private val forecastMap: MutableMap<String, MutableList<CurrentWeatherResponse>> = mutableMapOf()

    override fun loadCityForecast(city: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val forecastWeatherResponse = apiService.getForecastWeatherByCity(city).await()

            forecastWeatherResponse.forecastList.forEach {
                val key = getFormatedDate(it.dt)
                Log.i("ForecastPresenter", "--   ${key}")
                forecastMap.getOrPut(key){ mutableListOf()}+= it
            }

            forecastView.onForecastLoaded(forecastMap)

        }
    }

    override fun onDaySelected(day: Int) {
        val days = forecastMap.keys
        forecastView.showForecastForSelectedDay(forecastMap.get(days.elementAt(day))!!, day)
    }

    private fun getFormatedDate(time: Long): String{
        val sdf = java.text.SimpleDateFormat("dd-MM-yyyy")
        val date = java.util.Date(time * 1000)
        sdf.format(date)

        return sdf.format(date)
    }
}