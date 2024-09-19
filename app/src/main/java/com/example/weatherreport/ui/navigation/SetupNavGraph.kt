package com.example.weatherreport.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherreport.Route
import com.example.weatherreport.SearchScreen
import com.example.weatherreport.WeatherScreen
import com.example.weatherreport.viewmodel.WeatherViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: WeatherViewModel
) {
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
