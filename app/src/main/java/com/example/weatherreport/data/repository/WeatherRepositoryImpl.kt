package com.example.weatherreport.data.repository

import com.example.weatherreport.data.model.WeatherResponse
import com.example.weatherreport.data.service.WeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
): WeatherRepository {
    override suspend fun getWeather(cityName: String): WeatherResponse {
      return weatherService.getWeather(cityName, "ac304c2c6b1fd194fdae22dcfb0fe8ea")

    }
}