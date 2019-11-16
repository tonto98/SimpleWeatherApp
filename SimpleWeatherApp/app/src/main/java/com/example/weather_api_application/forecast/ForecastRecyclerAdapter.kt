package com.example.weather_api_application.forecast

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.weather_api_application.R
import com.example.weather_api_application.data.response.CurrentWeatherResponse

class ForecastRecyclerAdapter(var forecastList: MutableList<CurrentWeatherResponse>, val context: Context): RecyclerView.Adapter<ForecastRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.forecast_list_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val singleForecast: CurrentWeatherResponse = forecastList[p1]
        p0.time.text = singleForecast.dt_txt.substring(11,16)
        p0.description.text = singleForecast.weather[0].description
        p0.temperature.text = singleForecast.mainWeatherInfo.temp.toInt().toString() + "°C"
        p0.temperatureMin.text = singleForecast.mainWeatherInfo.tempMin.toInt().toString() + "°C"
        p0.temperatureMax.text = singleForecast.mainWeatherInfo.tempMax.toInt().toString() + "°C"
        Glide.with(context).load("http://openweathermap.org/img/wn/"+ singleForecast.weather[0].icon + "@2x.png").into(p0.weatherIcon)
    }

    fun update(forecastList: MutableList<CurrentWeatherResponse>){
        this.forecastList = forecastList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById<TextView>(R.id.textView_time_item_recycler_forecast)
        val description = itemView.findViewById<TextView>(R.id.textView_description_item_recycler_forecast)
        val temperature = itemView.findViewById<TextView>(R.id.textView_temperature_item_recycler_forecast)
        val temperatureMin = itemView.findViewById<TextView>(R.id.textView_temperatureMin_item_recycler_forecast)
        val temperatureMax = itemView.findViewById<TextView>(R.id.textView_temperatureMax_item_recycler_forecast)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.imageView_weather_item_recycler_forecast)
    }
}