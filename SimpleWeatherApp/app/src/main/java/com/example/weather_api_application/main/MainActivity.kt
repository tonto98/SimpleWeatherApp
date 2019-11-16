package com.example.weather_api_application.main

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.weather_api_application.R
import com.example.weather_api_application.citydata.CityListActivity
import com.example.weather_api_application.data.SharedPreference
import com.example.weather_api_application.data.response.CurrentWeatherResponse
import com.example.weather_api_application.forecast.ForecastActivity
import com.example.weather_api_application.main.presenter.MainPresenterImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity(), MainView {

    lateinit var mainPresenter: MainPresenterImpl
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    var selectedCity: String? = null

    private var permission = android.Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "called onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
        mainPresenter = MainPresenterImpl(this)

        textView_city_Current.setOnClickListener { selectCity() }
        locationPing_animation.setOnClickListener { handleLocationClick() }
        button_forecast_Current.setOnClickListener { showForecast() }

    }

    // initialize je u onStart umjesto onCreate kako bi se updateali podaci ako sa navigira u nazada do main-a
    override fun onStart() {
        Log.i("MainActivity", "called onStart")
        super.onStart()
        showLoading()
        selectedCity = SharedPreference(this).getValueString(SharedPreference.KEY_SELECTED_CITY)

        if(selectedCity != null){
            Log.i("MainActivity", selectedCity)
            mainPresenter.onWeatherRequestByCity(selectedCity!!)
        }else{
            Log.i("MainActivity", "nemam sharedpref")
            //ovdje bi trebao ici call an presentera koj rjesava check/requestLocation i getLocation
            getDataForCurrentLocation()
        }
    }

    fun getDataForCurrentLocation(){
        if (checkPermission(permission)) {
            getDeviceLocation()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(permission), PERMISSION_REQUEST)
            }else{
                Log.i("MainActivity", "stara verzija?")
            }
        }
    }

    override fun onWeatherApiResponse(currentWeatherResponse: CurrentWeatherResponse) {
        SharedPreference(this).save(SharedPreference.KEY_SELECTED_CITY,"${currentWeatherResponse.name}, ${currentWeatherResponse.sys.country}")

        textView_city_Current.text = applicationContext.getString(R.string.placeholder_city_country, currentWeatherResponse.name, currentWeatherResponse.sys.country)//"${currentWeatherResponse.name},  ${currentWeatherResponse.sys.country}"
        textView_temperature_Current.text = applicationContext.getString(R.string.placeholder_degree_celsius, currentWeatherResponse.mainWeatherInfo.temp.toInt())//currentWeatherResponse.mainWeatherInfo.temp.toInt().toString() + "°C"
        textView_wether_Current.text = currentWeatherResponse.weather[0].description
        textView_temperatureMax_Current.text = applicationContext.getString(R.string.placeholder_degree_celsius, currentWeatherResponse.mainWeatherInfo.tempMax.toInt())//currentWeatherResponse.mainWeatherInfo.tempMax.toInt().toString() + "°C"
        textView_temperatureMin_Current.text = applicationContext.getString(R.string.placeholder_degree_celsius, currentWeatherResponse.mainWeatherInfo.tempMin.toInt())//currentWeatherResponse.mainWeatherInfo.tempMin.toInt().toString() + "°C"
        textView_sunrise_Current.text = getFormatedDate(currentWeatherResponse.sys.sunrise, "HH:mm")
        textView_sunset_Current.text = getFormatedDate(currentWeatherResponse.sys.sunset, "HH:mm")
        textView_humidity_Current.text = applicationContext.getString(R.string.placeholder_percent, currentWeatherResponse.mainWeatherInfo.humidity.toInt())//currentWeatherResponse.mainWeatherInfo.humidity.toString() + "%"
        textView_cloudiness_Current.text = applicationContext.getString(R.string.placeholder_percent, currentWeatherResponse.clouds.all)
        textView_wind_Current.text = applicationContext.getString(R.string.placeholder_meters_per_second, currentWeatherResponse.wind.speed.toInt())
        textView_pressure_Current.text = applicationContext.getString(R.string.placeholder_hectopascal, currentWeatherResponse.mainWeatherInfo.pressure.toInt())
        textView_latestDate_Current.text = getFormatedDate(currentWeatherResponse.dt,"HH:mm:ss dd/MM/yy")
        Glide.with(this@MainActivity).load("http://openweathermap.org/img/wn/"+ currentWeatherResponse.weather[0].icon +"@2x.png").into(imageView_weatherIcon_Current)

        hideLoading()
    }

    private fun getFormatedDate(time: Long, pattern: String): String{
        val sdf = java.text.SimpleDateFormat(pattern)
        val date = java.util.Date(time * 1000)
        sdf.format(date)

        return sdf.format(date)
    }

    fun getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {

            var location: com.google.android.gms.tasks.Task<Location> = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener {
                if (it.isSuccessful){

                    var userLocation : Location = it.result
                    Log.i("MainActivity", "got location")
                    mainPresenter.onWeatherApiRequestByLocation(userLocation.latitude, userLocation.longitude)


                }else{
                    Log.i("MainActivity", "Cant get location")
                }
            }


        }catch (e : SecurityException){
            Log.i("MainActivity", "$e")
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess){
                Log.i("MainActivity", "dobio permission")
                getDeviceLocation()
            }else{
                Log.i("MainActivity", "ne valja")
            }

        }
    }

    fun selectCity(){
        val intent = Intent(this, CityListActivity::class.java)
        startActivity(intent)
    }

    fun showForecast(){
        val intent = Intent(this, ForecastActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading(){
        loading_animation_main.visibility = View.VISIBLE
        scrollView_main_content.visibility = View.INVISIBLE
    }

    override fun hideLoading(){
        loading_animation_main.visibility = View.INVISIBLE
        scrollView_main_content.visibility = View.VISIBLE
    }

    fun handleLocationClick(){
        showLoading()
        locationPing_animation.setProgress(0.toFloat())
        locationPing_animation.pauseAnimation()
        getDataForCurrentLocation()
    }
}
