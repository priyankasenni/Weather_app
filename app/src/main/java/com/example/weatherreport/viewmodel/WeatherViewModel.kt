package com.example.weatherreport.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherreport.data.WeatherRepository
import com.example.weatherreport.data.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel(){
    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    fun fetchWeather(cityName: String){
        viewModelScope.launch {
            try {
                val weatherResponse = weatherRepository.getWeather(cityName)
                _weatherState.value = weatherResponse
            }catch (e: Exception){
                Log.e("WeatherViewModel", "Error Message", e)
            }
        }
    }
}

//@HiltViewModel
//class WeatherViewModel @Inject constructor(
//    private val weatherRepository: WeatherRepository
//): ViewModel(){
//    fun calling() {
//        Log.e("Response",weatherRepository.toString())
//    }
//}