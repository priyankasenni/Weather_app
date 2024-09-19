package com.example.weatherreport.data.repository

import com.example.weatherreport.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(cityName: String): WeatherResponse
}