package com.example.weather_api_application.citydata.presenter

import com.example.weather_api_application.citydata.CityListView
import com.example.weather_api_application.citydata.model.Cities

class CityListPresenterImpl(var cityListView: CityListView,val cityList: List<Cities>): CityListPresenter {

    override fun initialLoad() {
        cityListView.onInitialLoad(cityList.sortedBy { it.country })
    }

    override fun loadSearchedList(searchString: String) {
        if (searchString == ""){
            cityListView.onLoad(cityList.sortedBy { it.country })
        }else{
            val result: List<Cities> = cityList.filter { s ->
                s.country.equals(searchString, ignoreCase = true) ||
                s.name.equals(searchString, ignoreCase = true) ||
                s.name.contains(searchString, ignoreCase = true) ||
                s.country.contains(searchString, ignoreCase = true)}
            cityListView.onLoad(result.sortedBy { it.name })
        }
    }
}