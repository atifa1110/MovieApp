package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.home.presentation.getFakeMovie
import com.example.movieapp.home.presentation.getFakeTvShow
import com.example.movieapp.list.presentation.ListScreen
import com.example.movieapp.list.presentation.ListUiState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    //Test Cases:
    //Checks if the upcoming movies list is displayed.
    //Checks if the popular movies list is displayed.
    //Checks if now playing movies list is displayed.
    //Checks if the popular tv show list is displayed.
    //Checks if now playing tv show list is displayed.
    //Ensures Movies list card can be clicked
    //Ensures Tv shows list card can be clicked
    @Test
    fun testUpcomingMoviesDisplayed() {
        val fakeMovies = listOf(
            getFakeMovie(1, "Upcoming 1"),
            getFakeMovie(2, "Upcoming 2"),
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(PagingData.from(fakeMovies)),
                    tvs = flowOf(),
                    mediaType = MediaType.Common.Movie.Upcoming
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = {}
            )
        }

        // Verify that the TopAppBar exists
        composeTestRule.onNodeWithTag("TopAppBar").assertExists()
        // Verify the title text is displayed correctly
        composeTestRule.onNodeWithText("Upcoming Movie").assertExists()
        // Verify that the UpcomingMoviesList is displayed
        composeTestRule.onNodeWithTag("UpcomingMoviesList").assertExists()
    }

    @Test
    fun testPopularMoviesDisplayed() {
        val fakeMovies = listOf(
            getFakeMovie(1, "Popular 1"),
            getFakeMovie(2," Popular 2")
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(PagingData.from(fakeMovies)),
                    tvs = flowOf(),
                    mediaType = MediaType.Common.Movie.Popular
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = {}
            )
        }

        // Verify that the TopAppBar exists
        composeTestRule.onNodeWithTag("TopAppBar").assertExists()
        // Verify the title text is displayed correctly
        composeTestRule.onNodeWithText("Most Popular Movie").assertExists()
        // Verify that the PopularMoviesList is displayed
        composeTestRule.onNodeWithTag("PopularMoviesList").assertExists()
    }

    @Test
    fun testNowPlayingMoviesDisplayed() {
        val fakeMovies = listOf(
            getFakeMovie(1, "Now Playing 1"),
            getFakeMovie(2, "Now Playing 2"),
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(PagingData.from(fakeMovies)),
                    tvs = flowOf(),
                    mediaType = MediaType.Common.Movie.NowPlaying
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = {}
            )
        }

        // Verify that the TopAppBar exists
        composeTestRule.onNodeWithTag("TopAppBar").assertExists()
        // Verify the title text is displayed correctly
        composeTestRule.onNodeWithText("Now Playing Movie").assertExists()
        // Verify that the NowPlayingMoviesList is displayed
        composeTestRule.onNodeWithTag("NowPlayingMoviesList").assertExists()
    }

    @Test
    fun testPopularTvShowDisplayed() {
        val fakeTvs = listOf(
            getFakeTvShow(1, "Popular 1"),
            getFakeTvShow(2," Popular 2")
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(),
                    tvs = flowOf(PagingData.from(fakeTvs)),
                    mediaType = MediaType.Common.TvShow.Popular
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = {}
            )
        }

        // Verify that the TopAppBar exists
        composeTestRule.onNodeWithTag("TopAppBar").assertExists()
        // Verify the title text is displayed correctly
        composeTestRule.onNodeWithText("Most Popular Tv").assertExists()
        // Verify that the PopularMoviesList is displayed
        composeTestRule.onNodeWithTag("PopularTvShowList").assertExists()
    }

    @Test
    fun testNowPlayingTvShowDisplayed() {
        val fakeTvs = listOf(
            getFakeTvShow(1, "Popular 1"),
            getFakeTvShow(2," Popular 2")
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(),
                    tvs = flowOf(PagingData.from(fakeTvs)),
                    mediaType = MediaType.Common.TvShow.NowPlaying
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = {}
            )
        }

        // Verify that the TopAppBar exists
        composeTestRule.onNodeWithTag("TopAppBar").assertExists()
        // Verify the title text is displayed correctly
        composeTestRule.onNodeWithText("Now Playing Tv").assertExists()
        // Verify that the NowPlayingMoviesList is displayed
        composeTestRule.onNodeWithTag("NowPlayingTvShowList").assertExists()
    }

    @Test
    fun verifyMovieItemClickNavigatesToDetailPage() {
        var clickedMovieId: Int? = null
        val fakeMovies = listOf(
            getFakeMovie(1, "Upcoming 1"),
            getFakeMovie(2, "Upcoming 2"),
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(PagingData.from(fakeMovies)),
                    tvs = flowOf(),
                    mediaType = MediaType.Common.Movie.Upcoming
                ),
                    onUpcomingClick = { movieId -> clickedMovieId = movieId },
                    onMovieClick = {},
                    onTvClick = {}
                )
        }

        // Verify that the PopularMoviesList is displayed
        composeTestRule.onNodeWithTag("UpcomingMoviesList").assertExists()
        // Verify Upcoming Card is exist and perform click
        composeTestRule.onNodeWithText("Upcoming 1").performClick()
        // Verify that the click callback was triggered with the correct movie ID
        assert(clickedMovieId == 1) { "Expected movie ID 1, but got $clickedMovieId" }
    }


    @Test
    fun verifyTvItemClickNavigatesToDetailPage() {
        var clickedTvShowId: Int? = null
        val fakeTvs = listOf(
            getFakeTvShow(1, "Popular 1"),
            getFakeTvShow(2," Popular 2")
        )

        composeTestRule.setContent {
            ListScreen(
                uiState = ListUiState(
                    movies = flowOf(),
                    tvs = flowOf(PagingData.from(fakeTvs)),
                    mediaType = MediaType.Common.TvShow.Popular
                ),
                onUpcomingClick = {},
                onMovieClick = {},
                onTvClick = { tvId -> clickedTvShowId = tvId }
            )
        }

        // Verify that the PopularTvsList is displayed
        composeTestRule.onNodeWithTag("PopularTvShowList").assertExists()
        // Find the first movie item and perform a click
        composeTestRule.onNodeWithText("Popular 1").performClick()
        // Verify that the click callback was triggered with the correct movie ID
        assert(clickedTvShowId == 1) { "Expected Tv ID 1, but got $clickedTvShowId" }
    }


}