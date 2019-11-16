package com.example.weather_api_application.data

import com.example.weather_api_application.data.response.CurrentWeatherResponse
import com.example.weather_api_application.data.response.ForecastWeatherReponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val API_KEY = "860b3cc7d7553387175273a01476e7e9"
val BASE_URL = "http://api.openweathermap.org/data/2.5/"
//  api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=860b3cc7d7553387175273a01476e7e9
//  http://  <--- ?

interface WeatherApiService {

    @GET("weather")
    fun getCurrentWeatherByCity(
            @Query("q") location :String,
            @Query("units") units: String = "metric"
    ): Deferred<CurrentWeatherResponse>


    @GET("weather")
    fun getCurrentWeatherByLocation(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("units") units: String = "metric"
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecastWeatherByCity(
            @Query("q") location :String,
            @Query("units") units: String = "metric"
    ): Deferred<ForecastWeatherReponse>

    companion object{
        operator fun invoke(): WeatherApiService{
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("APPID", API_KEY)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)

            }
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherApiService::class.java)
        }
    }
}