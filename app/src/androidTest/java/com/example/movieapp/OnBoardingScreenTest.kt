package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.onboarding.OnBoardingScreen
import com.example.movieapp.ui.theme.MovieAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnBoardingScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var isNavigated = false
    private var isOnBoardingStateSet = false

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MovieAppTheme {
                OnBoardingScreen(
                    setOnBoardingState = { isOnBoardingStateSet = true },
                    onNavigateToAuth = { isNavigated = true }
                )
            }
        }
    }

    @Test
    fun verifyOnboardingScreenDisplaysCorrectly() {
        // Check if first page content is displayed
        composeTestRule.onNodeWithText("The biggest international and local film streaming")
            .assertIsDisplayed()
    }

    @Test
    fun verifyPagingThroughOnboardingScreens() {
        val nextButton = composeTestRule.onNode(hasContentDescriptionExactly("Next Button"))

        // Navigate to second page
        nextButton.performClick()
        composeTestRule.onNodeWithText("Offers ad-free viewing of high quality")
            .assertIsDisplayed()

        // Navigate to third page
        nextButton.performClick()
        composeTestRule.onNodeWithText("Our service brings to your favorite series")
            .assertIsDisplayed()
    }

    @Test
    fun verifyFinalPageNavigatesToAuth() {
        val nextButton = composeTestRule.onNode(hasContentDescriptionExactly("Next Button"))

        // Navigate to third page
        repeat(2) { nextButton.performClick() }

        // Click final next button to trigger navigation
        nextButton.performClick()

        assert(isNavigated)
        assert(isOnBoardingStateSet)
    }

}