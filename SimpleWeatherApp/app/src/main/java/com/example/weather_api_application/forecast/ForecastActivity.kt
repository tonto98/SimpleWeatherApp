package com.example.weather_api_application.forecast

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.weather_api_application.R
import com.example.weather_api_application.citydata.CityListActivity
import com.example.weather_api_application.data.SharedPreference
import com.example.weather_api_application.data.response.CurrentWeatherResponse
import com.example.weather_api_application.forecast.presenter.ForecastPresenterImpl
import com.example.weather_api_application.main.MainActivity
import kotlinx.android.synthetic.main.activity_forecast.*
import java.text.SimpleDateFormat


class ForecastActivity : AppCompatActivity(), ForecastView {

    private val ICON_URL_BASE = "http://openweathermap.org/img/wn/"
    private val ICON_URL_END = "@2x.png"

    private lateinit var adapter: ForecastRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        forecast_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        showLoading()
    }

    override fun onStart(){
        super.onStart()

        val selectedCity = SharedPreference(this).getValueString(SharedPreference.KEY_SELECTED_CITY)?: "Rijeka,hr"

        val forecastPresenter = ForecastPresenterImpl(this)
        forecastPresenter.loadCityForecast(selectedCity)

        forecast_title_text_view.text = selectedCity
        forecast_title_text_view.setOnClickListener { selectCity() }

        linearLayout_forecast_container_day0.setOnClickListener { forecastPresenter.onDaySelected(0) }
        linearLayout_forecast_container_day1.setOnClickListener { forecastPresenter.onDaySelected(1) }
        linearLayout_forecast_container_day2.setOnClickListener { forecastPresenter.onDaySelected(2) }
        linearLayout_forecast_container_day3.setOnClickListener { forecastPresenter.onDaySelected(3) }
        linearLayout_forecast_container_day4.setOnClickListener { forecastPresenter.onDaySelected(4) }
    }

    override fun onForecastLoaded(forecastMap: MutableMap<String, MutableList<CurrentWeatherResponse>>) {
        //textView_forecast_test.text = forecastMap.get("2019-07-23").toString()
        val dates = forecastMap.keys

        textView_date_forecast_container_day0.text = dates.elementAt(0).removeRange(5,10)
        textView_date_forecast_container_day1.text = dates.elementAt(1).removeRange(5,10)
        textView_date_forecast_container_day2.text = dates.elementAt(2).removeRange(5,10)
        textView_date_forecast_container_day3.text = dates.elementAt(3).removeRange(5,10)
        textView_date_forecast_container_day4.text = dates.elementAt(4).removeRange(5,10)

        textView_day_forecast_container_day0.text = getDayOfWeek(dates.elementAt(0))
        textView_day_forecast_container_day1.text = getDayOfWeek(dates.elementAt(1))
        textView_day_forecast_container_day2.text = getDayOfWeek(dates.elementAt(2))
        textView_day_forecast_container_day3.text = getDayOfWeek(dates.elementAt(3))
        textView_day_forecast_container_day4.text = getDayOfWeek(dates.elementAt(4))

        Glide.with(this).load(ICON_URL_BASE + forecastMap.get(dates.elementAt(0))!![0].weather[0].icon + ICON_URL_END).into(imageView_weather_icon_container_day0)
        Glide.with(this).load(ICON_URL_BASE + forecastMap.get(dates.elementAt(1))!![4].weather[0].icon + ICON_URL_END).into(imageView_weather_icon_container_day1)
        Glide.with(this).load(ICON_URL_BASE + forecastMap.get(dates.elementAt(2))!![4].weather[0].icon + ICON_URL_END).into(imageView_weather_icon_container_day2)
        Glide.with(this).load(ICON_URL_BASE + forecastMap.get(dates.elementAt(3))!![4].weather[0].icon + ICON_URL_END).into(imageView_weather_icon_container_day3)
        Glide.with(this).load(ICON_URL_BASE + forecastMap.get(dates.elementAt(4))!![4].weather[0].icon + ICON_URL_END).into(imageView_weather_icon_container_day4)

        // defaultno selectan trenutni dan
        adapter = ForecastRecyclerAdapter(forecastMap.get(dates.elementAt(0))!!, this)
        forecast_recycler_view.adapter = adapter
        Log.i("ForecastActivity", "created adapter")

        showForecastForSelectedDay(forecastMap.get(dates.elementAt(0))!!, 0)

        hideLoading()
    }

    override fun showForecastForSelectedDay(forecastList: MutableList<CurrentWeatherResponse>, day: Int) {
        highlightSelectedDay(day)
        adapter.update(forecastList)
        Log.i("ForecastActivity", "updated adapter")
    }

    fun getDayOfWeek(date: String): String{

        val format1 = SimpleDateFormat("dd-MM-yyyy")
        val dt1 = format1.parse(date)
        val format2 = SimpleDateFormat("EEEE")
        var finalDay = format2.format(dt1)
        when(finalDay){
            "Monday"    ->  {finalDay = "MON"}
            "Tuesday"   ->  {finalDay = "TUE"}
            "Wednesday"   ->  {finalDay = "WED"}
            "Thursday"   ->  {finalDay = "THU"}
            "Friday"   ->  {finalDay = "FRI"}
            "Saturday"   ->  {finalDay = "SAT"}
            "Sunday"   ->  {finalDay = "SUN"}
        }


        return finalDay
    }

    fun highlightSelectedDay(day: Int){
        removeDayHighlights()
        when(day){
            0 -> linearLayout_forecast_container_day0.setBackgroundColor(ContextCompat.getColor(this, R.color.appBackground))
            1 -> linearLayout_forecast_container_day1.setBackgroundColor(ContextCompat.getColor(this, R.color.appBackground))
            2 -> linearLayout_forecast_container_day2.setBackgroundColor(ContextCompat.getColor(this, R.color.appBackground))
            3 -> linearLayout_forecast_container_day3.setBackgroundColor(ContextCompat.getColor(this, R.color.appBackground))
            4 -> linearLayout_forecast_container_day4.setBackgroundColor(ContextCompat.getColor(this, R.color.appBackground))
        }
    }

    fun removeDayHighlights(){
        linearLayout_forecast_container_day0.background = null
        linearLayout_forecast_container_day1.background = null
        linearLayout_forecast_container_day2.background = null
        linearLayout_forecast_container_day3.background = null
        linearLayout_forecast_container_day4.background = null
    }

    fun selectCity(){
        val intent = Intent(this, CityListActivity::class.java)
        startActivity(intent)
    }


    fun showLoading(){
        loading_animation_forecast.visibility = View.VISIBLE
        linearLayout_forecast_container.visibility = View.INVISIBLE
        forecast_recycler_view.visibility = View.INVISIBLE
    }

    fun hideLoading(){
        loading_animation_forecast.visibility = View.INVISIBLE
        linearLayout_forecast_container.visibility = View.VISIBLE
        forecast_recycler_view.visibility = View.VISIBLE
    }

}
