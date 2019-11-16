package com.example.weather_api_application.citydata.presenter

interface CityListPresenter {
    fun initialLoad()
    fun loadSearchedList(searchString: String)
}