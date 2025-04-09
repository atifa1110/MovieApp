package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.profile.presentation.ProfileScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Test Cases :
    // verify display username and email correctly
    // click logout button to auth screen

    @Test
    fun profileScreenDisplaysUserProfile() {
        composeTestRule.setContent {
            ProfileScreen(
                userPhoto = "",
                userName = "Atifa Fiorenza",
                userEmail = "atifafiorenza24@gmail.com",
                onLogout = {},
                onNavigateToAuth = {},
                onNavigateToEditProfile = {}
            )
        }

        composeTestRule.onNodeWithText("Atifa Fiorenza").assertIsDisplayed()
        composeTestRule.onNodeWithText("atifafiorenza24@gmail.com").assertIsDisplayed()
    }

    @Test
    fun clickLogoutButtonToLoginPage() {
        var isLoggedOut = false

        composeTestRule.setContent {
            ProfileScreen(
                userPhoto = "",
                userName = "Atifa Fiorenza",
                userEmail = "atifafiorenza24@gmail.com",
                onLogout = { isLoggedOut = true },
                onNavigateToAuth = {},
                onNavigateToEditProfile = {}
            )
        }

        composeTestRule.onNodeWithText("Atifa Fiorenza").assertIsDisplayed()
        composeTestRule.onNodeWithText("atifafiorenza24@gmail.com").assertIsDisplayed()
        // Manually scroll to the bottom
        composeTestRule.onNodeWithTag("ScrollableColumn").performTouchInput { swipeUp() }
        composeTestRule.onNodeWithTag("LogoutButton").assertIsDisplayed().performClick()

        assertTrue(isLoggedOut)
    }

}