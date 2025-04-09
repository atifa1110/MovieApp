package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.detail.presentation.DetailScreen
import com.example.movieapp.detail.presentation.DetailsUiState
import com.example.movieapp.home.presentation.getFakeDetailMovie
import com.example.movieapp.home.presentation.getFakeDetailTvShow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    //Test Cases:
    //Check if movie details are displayed correctly
    //Check if tv show details are displayed correctly
    //Check if trailer details are displayed correctly
    //Check if overview less button can be click
    //Check if cast, crew, and gallery can be touch
    //Clicking the wishlist button should update the state
    //Clicking the back button should trigger navigation

    @Test
    fun movieDetailsDisplayedCorrectly() {
        composeTestRule.setContent {
                DetailScreen(
                    uiState = DetailsUiState(
                        mediaType = MediaType.Details.from(1,"movie"),
                        movie = getFakeDetailMovie(),
                        isLoading = false,
                        userMessage = null
                    ),
                    snackBarHostState = SnackbarHostState(),
                    snackBarMessageShown = {},
                    addMovieToWishlist = {},
                    addTvShowToWishlist = {},
                    onBackButton = {}
                )
        }

        // Verify title is displayed
        composeTestRule.onNodeWithText("Avatar Ages: Dreams").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        // Verify runtime, release date, and rating
        composeTestRule.onNodeWithText("32 Minutes").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.2").assertIsDisplayed()
        // Verify play button exists
        composeTestRule.onNodeWithTag("Play").assertExists()
    }

    @Test
    fun tvShowDetailsDisplayedCorrectly() {
        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailsUiState(
                    mediaType = MediaType.Details.from(1,"tv_show"),
                    tvShow = getFakeDetailTvShow(),
                    isLoading = false,
                    userMessage = null
                ),
                snackBarHostState = SnackbarHostState(),
                snackBarMessageShown = {},
                addMovieToWishlist = {},
                addTvShowToWishlist = {},
                onBackButton = {}
            )
        }

        // Verify title is displayed
        composeTestRule.onNodeWithText("Avatar Ages: Dreams").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TvShowDetailsItemList").assertExists()
        composeTestRule.onNodeWithTag("TvShowDetailsItemList").performScrollToIndex(5)
        // Verify runtime, release date, and rating
        composeTestRule.onNodeWithText("Season 1").assertIsDisplayed()
        // Verify play button exists
        //composeTestRule.onNodeWithText("Play").assertExists()
    }

    @Test
    fun trailersDetailsDisplayedCorrectly() {
        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailsUiState(
                    mediaType = MediaType.Details.from(1,"trailers"),
                    movie = getFakeDetailMovie(),
                    isLoading = false,
                    userMessage = null
                ),
                snackBarHostState = SnackbarHostState(),
                snackBarMessageShown = {},
                addMovieToWishlist = {},
                addTvShowToWishlist = {},
                onBackButton = {}
            )
        }

        composeTestRule.onNodeWithTag("DetailTopAppBar").assertExists()
        composeTestRule.onNodeWithText("Trailer").assertExists()
        // Verify title is displayed
        composeTestRule.onNodeWithText("Avatar Ages: Dreams").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        // Verify runtime, release date, and rating
        composeTestRule.onNodeWithTag("VideoPlayer").assertIsDisplayed()
    }

    @Test
    fun clickingWishlistButtonUpdatesWishlistState() {
        val isWishlist = mutableStateOf(false)

        composeTestRule.setContent {
                DetailScreen(
                    uiState = DetailsUiState(
                        mediaType = MediaType.Details.from(1,"movie"),
                        movie = getFakeDetailMovie(),
                        isLoading = false,
                        userMessage = null
                    ),
                    snackBarHostState = SnackbarHostState(),
                    snackBarMessageShown = {},
                    addMovieToWishlist = { isWishlist.value = !isWishlist.value },
                    addTvShowToWishlist = {},
                    onBackButton = {}
                )
            }

        composeTestRule.onNodeWithTag("DetailTopAppBar").assertExists()
        composeTestRule.onNodeWithTag("WishlistButton").assertExists()
        // Initially, the wishlist button should not be active
        composeTestRule.onNodeWithContentDescription("Wishlist Button").assertIsDisplayed()
        // Click the wishlist button
        composeTestRule.onNodeWithContentDescription("Wishlist Button").performClick()
        // Verify wishlist state is updated
        assert(isWishlist.value)
    }

}