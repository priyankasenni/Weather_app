package com.example.weatherreport

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.weatherreport.ui.navigation.SetupNavGraph
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Cyan, Color.Gray
                                    )
                                )
                            )
                    ) {
                        val navController = rememberNavController()
                        SetupNavGraph(navController = navController, viewModel = viewModel)
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
            onClick = { navigateToWeatherScreen(cityName) },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Search Weather",
                style = MaterialTheme.typography.titleMedium
            )
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
        Text(
            "Weather in ${cityName.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        when {
            weatherState == null -> Text("Loading...")
            else -> {
                val weather = weatherState?.weather?.firstOrNull()
                val main = weatherState?.main
                val iconUrl = "http://openweathermap.org/img/wn/${weather?.icon}.png"

                Card(
                    modifier = Modifier.padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val temperatureCelsius =
                            main?.temp?.minus(273.15)?.let { "%.1f".format(it) }

                        Text(
                            "Temperature: ${temperatureCelsius}°C",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Description: ${weather?.description}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Log.d("WeatherScreen", "Icon URL: $iconUrl")

                SubcomposeAsyncImage(
                    model = iconUrl,
                    loading = {
                        CircularProgressIndicator(
                            color = Color.Blue
                        )
                    },
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = navigateBack,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Back",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

