package com.example.weatherreport.data

import com.example.weatherreport.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q")cityName: String,
        @Query("appid")apiKey: String
    ): WeatherResponse
}