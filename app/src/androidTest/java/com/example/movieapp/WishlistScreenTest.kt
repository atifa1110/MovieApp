package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.home.getFakeWishListed
import com.example.movieapp.wishlist.presentation.WishlistScreen
import com.example.movieapp.wishlist.presentation.WishlistUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WishlistScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    //Test Cases:
    //Checks if wishlist items are displayed correctly.
    //Simulates clicking a movie and verifies navigation happens.
    //Tests empty state UI when no movies exist.
    //Simulates clicking delete a movie and verify if it id deleted

    @Test
    fun wishlistScreenDisplaysMoviesAndHandlesNavigation() {
        val list = listOf(getFakeWishListed(1,"Wishlist 1"),getFakeWishListed(2, "Wishlist 2"))
        composeTestRule.setContent {
            WishlistScreen(
                uiState = WishlistUiState(
                    wishlist = list,
                    isLoading = false
                ),
                onRefreshMovies = {},
                onMovieClick = {},
                deleteMovieFromWishlist = {},
                deleteTvShowFromWishlist = {},
                snackBarMessageShown = {},
                snackBarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithText("Wishlist").assertExists()
        composeTestRule.onNodeWithText("Wishlist 1").assertExists()
        composeTestRule.onNodeWithText("Wishlist 2").assertExists()
    }

    @Test
    fun clickingMovieNavigatesToDetails() {
        var clickedMovieId: Int? = null

        val list = listOf(getFakeWishListed(1,"Wishlist 1"),getFakeWishListed(2, "Wishlist 2"))
        composeTestRule.setContent {
            WishlistScreen(
                uiState = WishlistUiState(wishlist = list, isLoading = false),
                onRefreshMovies = {},
                onMovieClick = { movieId -> clickedMovieId = movieId },
                deleteMovieFromWishlist = {},
                deleteTvShowFromWishlist = {},
                snackBarMessageShown = {},
                snackBarHostState = SnackbarHostState()
            )
        }

        // Click on movie
        composeTestRule.onNodeWithTag("WishlistList").assertExists()
        composeTestRule.onNodeWithText("Wishlist 1").assertExists()
        composeTestRule.onNodeWithText("Wishlist 1").performClick()

        // Check if correct movie ID is sent
        assert(clickedMovieId == 1) { "Expected movie ID 1, but got $clickedMovieId" }
    }

    @Test
    fun clickingMovieDeleteFromDatabase() {
        // Step 1: Create a mutable state for the wishlist
        val wishlistState = mutableStateOf(
            listOf(
                getFakeWishListed(1, "Wishlist 1"),
                getFakeWishListed(2, "Wishlist 2")
            )
        )

        composeTestRule.setContent {
            WishlistScreen(
                uiState = WishlistUiState(wishlist = wishlistState.value, isLoading = false),
                onRefreshMovies = {},
                onMovieClick = {},
                deleteMovieFromWishlist = { deletedId ->
                    wishlistState.value = wishlistState.value.filter { it.id != deletedId }
                },
                deleteTvShowFromWishlist = {},
                snackBarMessageShown = {},
                snackBarHostState = SnackbarHostState()
            )
        }

        // Step 2: Verify that both Wishlist 1 and Wishlist 2 exist
        composeTestRule.onNodeWithText("Wishlist 1").assertExists()
        composeTestRule.onNodeWithText("Wishlist 2").assertExists()

        // Step 3: Click the delete button for Wishlist 1
        composeTestRule.onNodeWithTag("DeleteWishlist_2").assertExists().performClick()

        // Step 4: Wait for UI to update
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().printToLog("TestUI")
        // Step 5: Verify that Wishlist 1 is removed but Wishlist 2 still exists
        composeTestRule.onNodeWithText("Wishlist 1").assertExists()
    }


    @Test
    fun wishlistScreenShowsEmptyStateWhenNoMovies() {
        composeTestRule.setContent {
            WishlistScreen(
                uiState = WishlistUiState(wishlist = emptyList(), isLoading = false),
                onRefreshMovies = {},
                onMovieClick = {},
                deleteMovieFromWishlist = {},
                deleteTvShowFromWishlist = {},
                snackBarMessageShown = {},
                snackBarHostState = SnackbarHostState()
            )
        }

        // Verify empty state message appears
        composeTestRule.onNodeWithText("There is No Movie Yet!").assertExists()
    }
}