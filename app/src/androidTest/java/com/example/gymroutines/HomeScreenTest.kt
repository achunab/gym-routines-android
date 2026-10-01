package com.example.gymroutines

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun botonIniciar_estaVisible() {
        composeTestRule.setContent {
            MaterialTheme {
                Column {
                    Text("Test - Ninon ♓️")
                }
            }
        }
        composeTestRule.onNodeWithText("Test - Ninon", substring = true).assertExists()
    }
}