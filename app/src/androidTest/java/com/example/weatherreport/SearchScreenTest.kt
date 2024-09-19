package com.example.weatherreport

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSearchFunctionality() {
        var searchedCity = ""
        val navigateToWeatherScreen: (String) -> Unit = { city -> searchedCity = city }

        composeTestRule.setContent {
            SearchScreen(navigateToWeatherScreen = navigateToWeatherScreen)
        }

        composeTestRule.onNodeWithText("Enter city").performTextInput("London")
        composeTestRule.onNodeWithText("Search Weather").performClick()

        assert(searchedCity == "London")
    }
}
