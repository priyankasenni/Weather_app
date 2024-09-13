package com.example.weatherreport.data

interface WeatherRepository {
    suspend fun getWeather(cityName: String): WeatherResponse
}