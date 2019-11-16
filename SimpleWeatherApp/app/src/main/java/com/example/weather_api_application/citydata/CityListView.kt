package com.example.weather_api_application.citydata

import com.example.weather_api_application.citydata.model.Cities

interface CityListView {
    fun onLoad(cityList: List<Cities>)
    fun onInitialLoad(cityList: List<Cities>)
}