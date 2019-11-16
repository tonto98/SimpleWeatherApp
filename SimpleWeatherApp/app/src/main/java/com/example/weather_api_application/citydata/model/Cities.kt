package com.example.weather_api_application.citydata.model

import com.example.weather_api_application.data.response.Coord

data class Cities(
        val id: Int,
        val name: String,
        val country: String,
        val coord: Coord
)