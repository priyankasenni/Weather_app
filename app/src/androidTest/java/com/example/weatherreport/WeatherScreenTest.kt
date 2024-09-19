package com.example.weatherreport

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNavigateBack() {
        var backPressed = false
        val navigateBack = { backPressed = true }

        composeTestRule.setContent {
            SimpleWeatherScreen(cityName = "London", navigateBack = navigateBack)
        }

        composeTestRule.onNodeWithText("Back").performClick()

        assert(backPressed)
    }
}

@Composable
fun SimpleWeatherScreen(cityName: String, navigateBack: () -> Unit) {
    Column {
        Text(text = "Weather in $cityName")
        Button(onClick = navigateBack) {
            Text("Back")
        }
    }
}
