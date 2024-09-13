package com.example.weatherreport

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weatherreport.ui.theme.WeatherReportTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherreport.viewmodel.WeatherViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherReportTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Route.SEARCH_SCREEN) {
                    composable(route = Route.SEARCH_SCREEN) {
                        SearchScreen(
                            navigateToWeatherScreen = { cityName ->
                                navController.navigate("weatherScreen/$cityName")
                            }
                        )
                    }
                    composable(route = Route.WEATHER_SCREEN) { backStackEntry ->
                        val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
                        WeatherScreen(
                            cityName = cityName,
                            navigateBack = {
                                navController.popBackStack()
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

object Route {
    const val SEARCH_SCREEN = "searchScreen"
    const val WEATHER_SCREEN = "weatherScreen/{cityName}"
}

@Composable
fun SearchScreen(
    navigateToWeatherScreen: (String) -> Unit
) {
    var cityName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter city") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navigateToWeatherScreen(cityName) }
        ) {
            Text("Current Weather")
        }
    }
}


@Composable
fun WeatherScreen(
    cityName: String,
    navigateBack: () -> Unit,
    viewModel: WeatherViewModel

) {
    val weatherState by viewModel.weatherState.collectAsState()

    LaunchedEffect(cityName) {
        viewModel.fetchWeather(cityName)
    }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Weather in $cityName")
        Spacer(modifier = Modifier.height(16.dp))
        when {
            weatherState == null -> Text("Loading...")
            else -> {
                val weather = weatherState?.weather?.firstOrNull()
                val main = weatherState?.main
                val iconUrl = "http://openweathermap.org/img/wn/${weather?.icon}.png"

                Text("Temperature: ${main?.temp}°C")
                Text("Description: ${weather?.description}")

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = iconUrl,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateBack) {
            Text("Back")
        }
    }
}
