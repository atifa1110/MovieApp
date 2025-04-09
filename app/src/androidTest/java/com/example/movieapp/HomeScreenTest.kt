package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.model.TvShow
import com.example.movieapp.home.presentation.HomeScreen
import com.example.movieapp.home.presentation.HomeUiState
import com.example.movieapp.home.presentation.getFakeMovie
import com.example.movieapp.home.presentation.getFakeTvShow
import com.example.movieapp.ui.theme.MovieAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeMovies: Map<MediaType.Movie, List<Movie>>
    private lateinit var fakeTvs: Map<MediaType.TvShow, List<TvShow>>
    @Before
    fun setup() {
        fakeMovies = mapOf(
            MediaType.Movie.Upcoming to listOf(getFakeMovie()),
            MediaType.Movie.Popular to listOf(getFakeMovie()),
            MediaType.Movie.NowPlaying to listOf(getFakeMovie())
        )

        fakeTvs = mapOf(
            MediaType.TvShow.Popular to listOf(getFakeTvShow()),
            MediaType.TvShow.NowPlaying to listOf(getFakeTvShow())
        )

        composeTestRule.setContent {
            MovieAppTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        userId = null,
                        movies = fakeMovies,
                        tvShows = fakeTvs,
                        genres = listOf("Adventure", "Fantasy", "Animation", "Action"),
                        loadStates = mapOf(
                            MediaType.Movie.Popular to false,
                            MediaType.Movie.NowPlaying to false
                        ),
                        selectedCategory = "Adventure",
                        errorMessage = null,
                        isOfflineModeAvailable = false
                    ),
                    onSelected = {},
                    onSeeFavoriteClick = {},
                    onSeeAllClick = {},
                    onMovieClick = {},
                    onTvShowClick = {},
                    onRefresh = {},
                    snackBarHostState = remember { SnackbarHostState() }
                )
            }
        }
    }


    //Test Cases:
    //Verify if HomeScreen is displayed
    //Check if LazyColumn scrolls properly
    //Check if Movie and TV Show sections are displayed
    //Check interaction with category selection
    //Check if refresh action works
    //verify card click to list page
    //verify favorite click to favorite page

    @Test
    fun clickTopAppBarFavoriteDisplays() {
        // Check if appbar, lazy column, upcoming, category
        composeTestRule.onNodeWithTag("HomeTopAppBar").assertExists()
        composeTestRule.onNodeWithTag("WishlistButton").assertExists().performClick()
    }

    @Test
    fun verifyHomeScreenDisplaysMovies() {
        // Check if appbar, lazy column, upcoming, category
        composeTestRule.onNodeWithText("Hello, null").assertIsDisplayed()
        composeTestRule.onNodeWithTag("my_lazy_column").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upcoming Movie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()
    }

    @Test
    fun verifyMoviesAndTvShowsDisplayed() {
        composeTestRule.onNodeWithText("Most Popular Movie").assertIsDisplayed()
        // Perform swipe up multiple times until the text is found
        composeTestRule.onNodeWithTag("my_lazy_column").performScrollToIndex(3)
        // Assert that the text is now visible on the screen
        composeTestRule.onNodeWithText("Most Popular Tv").assertIsDisplayed()
    }

    @Test
    fun verifyGenreSelection() {
        composeTestRule.onNodeWithText("Fantasy").performClick()
        composeTestRule.onNodeWithText("Fantasy")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun verifyScrollFunctionality() {
        composeTestRule.onNodeWithTag("my_lazy_column").performScrollToIndex(5)
    }

    @Test
    fun verifySwipeToRefreshLoading() {

        // Ensure SwipeRefresh exists
        composeTestRule.onNodeWithTag("SwipeRefresh").assertExists()

        // Perform swipe-down to trigger refresh
        composeTestRule.onNodeWithTag("SwipeRefresh").performTouchInput {
            swipeDown()
        }


        // Verify loading indicator is displayed
        composeTestRule.onNodeWithTag("RefreshIndicator").assertExists()


        // Wait for refresh to complete
        composeTestRule.waitForIdle()

        // Verify loading indicator disappears
        composeTestRule.onNodeWithTag("RefreshIndicator").assertDoesNotExist()
    }

}