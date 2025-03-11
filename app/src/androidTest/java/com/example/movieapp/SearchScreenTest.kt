package com.example.movieapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movieapp.core.model.Movie
import com.example.movieapp.home.getFakeMovie
import com.example.movieapp.home.getFakeTvShow
import com.example.movieapp.login.presentation.LoginUiState
import com.example.movieapp.search.presentation.HistoryUiState
import com.example.movieapp.search.presentation.SearchScreen
import com.example.movieapp.search.presentation.SearchUiState
import com.example.movieapp.search.presentation.TrendingMovieUiState
import com.example.movieapp.search.presentation.TrendingTvUiState
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeMovies: List<Movie>

    // Test Cases :
    // verify movie and tv recommendation is display
    // click movie recommendation to detail
    // click tv show recommendation to detail
    // input search is not exist show error (we are sorry we can not find the movie :(
    // input search is exist show list movie
    // click cancel search button and show recommendation list
    // click search card add to history and show list & snack-bar
    // delete history button and show snack-bar

    @Before
    fun setup() {
        fakeMovies = listOf(
            getFakeMovie(1,"Popular Movie Search"),
            getFakeMovie(2,"Now Playing Movie Search"),
            getFakeMovie(3,"Upcoming Movie Search"),
            getFakeMovie(4,"Trending Movie Search")
        )
    }

    @Test
    fun verifyRecommendationIsDisplayed() {
        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember { mutableStateOf(SearchUiState(
                    trendingMovieUiState = TrendingMovieUiState(
                        trendingMovies = listOf(getFakeMovie(1,"Trending Movies 1"),getFakeMovie(3,"Trending Movies 2")),
                        isTrendingMovie = false
                    ),
                    trendingTvUiState = TrendingTvUiState(
                        trendingTv = listOf(getFakeTvShow(1,"Trending Tv 1"),getFakeTvShow(2,"Trending Tv 2")),
                        isTrendingTv = false
                    )
                )) }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = flowOf(PagingData.from(fakeMovies)).collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = {},
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Verify that search bar is displayed
        composeTestRule.onNodeWithTag("MovieContainer").assertExists()
        composeTestRule.onNodeWithTag("Movie Recommendation for you").assertExists()
        composeTestRule.onNodeWithTag("ContainerLazyRowMovie").assertExists()
        composeTestRule.onNodeWithText("Trending Movies 1").assertIsDisplayed()

        composeTestRule.onNodeWithTag("TvContainer").assertExists()
        composeTestRule.onNodeWithTag("Tv Recommendation for you").assertExists()
        composeTestRule.onNodeWithTag("ContainerLazyRowTv").assertExists()
        composeTestRule.onNodeWithText("Trending Tv 1").assertIsDisplayed()
    }

    @Test
    fun clickMovieCardRecommendationToDetail() {
        var navigatedMovieId: Int? = null

        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember { mutableStateOf(SearchUiState(
                    trendingMovieUiState = TrendingMovieUiState(
                        trendingMovies = listOf(getFakeMovie(1,"Trending Movies 1"),getFakeMovie(3,"Trending Movies 2")),
                        isTrendingMovie = false
                    ),
                    trendingTvUiState = TrendingTvUiState(
                        trendingTv = listOf(getFakeTvShow(1,"Trending Tv 1"),getFakeTvShow(2,"Trending Tv 2")),
                        isTrendingTv = false
                    )
                )) }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = flowOf(PagingData.from(fakeMovies)).collectAsLazyPagingItems(),
                    onMovieClick = { movieId -> navigatedMovieId = movieId },
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = {},
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Verify that movie container is display
        composeTestRule.onNodeWithTag("MovieContainer").assertExists()
        composeTestRule.onNodeWithTag("Movie Recommendation for you").assertExists()
        composeTestRule.onNodeWithTag("ContainerLazyRowMovie").assertExists()
        composeTestRule.onNodeWithText("Trending Movies 1").assertIsDisplayed().performClick()

        // Verify navigation happened (Movie ID should be set)
        assert(navigatedMovieId == 1)
    }

    @Test
    fun clickTvCardRecommendationToDetail() {
        var navigatedTvId: Int? = null

        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember { mutableStateOf(SearchUiState(
                    trendingMovieUiState = TrendingMovieUiState(
                        trendingMovies = listOf(getFakeMovie(1,"Trending Movies 1"),getFakeMovie(3,"Trending Movies 2")),
                        isTrendingMovie = false
                    ),
                    trendingTvUiState = TrendingTvUiState(
                        trendingTv = listOf(getFakeTvShow(1,"Trending Tv 1"),getFakeTvShow(2,"Trending Tv 2")),
                        isTrendingTv = false
                    )
                )) }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = flowOf(PagingData.from(fakeMovies)).collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = { tvId -> navigatedTvId = tvId },
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = {},
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Verify that tv container is display
        composeTestRule.onNodeWithTag("TvContainer").assertExists()
        composeTestRule.onNodeWithTag("Tv Recommendation for you").assertExists()
        composeTestRule.onNodeWithTag("ContainerLazyRowTv").assertExists()
        composeTestRule.onNodeWithText("Trending Tv 1").assertIsDisplayed().performClick()

        assert(navigatedTvId == 1)
    }

    @Test
    fun searchInputShowsMoviesDisplay() {
        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember {
                    mutableStateOf(
                        SearchUiState(
                            trendingMovieUiState = TrendingMovieUiState(
                                trendingMovies = listOf(getFakeMovie(1, "Trending Movies 1")),
                                isTrendingMovie = false
                            ),
                            trendingTvUiState = TrendingTvUiState(
                                trendingTv = listOf(getFakeTvShow(1, "Trending Tv 1")),
                                isTrendingTv = false
                            ),
                            query = "",
                        )
                    )
                }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = uiState.value.searchMovies.collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = { query ->
                        uiState.value = uiState.value.copy(
                            query = query,
                            isSearching = true,
                            searchMovies = flowOf(PagingData.from(fakeMovies)) // Ensure searchMovies updates with relevant results
                        )
                    },
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Perform text input (case-insensitive search)
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("trending")

        // Wait for UI update
        composeTestRule.waitForIdle()

        // Verify SearchLazyColumn exists
        composeTestRule.onNodeWithTag("SearchLazyColumn").assertExists()

        // Check if searched movie appears
        composeTestRule.onNodeWithText("Trending Movie Search", ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun searchInputShowsMoviesError() {
        composeTestRule.setContent {

            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember {
                    mutableStateOf(
                        SearchUiState(
                            trendingMovieUiState = TrendingMovieUiState(
                                trendingMovies = listOf(getFakeMovie(1, "Trending Movies 1")),
                                isTrendingMovie = false
                            ),
                            trendingTvUiState = TrendingTvUiState(
                                trendingTv = listOf(getFakeTvShow(1, "Trending Tv 1")),
                                isTrendingTv = false
                            ),
                            query = "",
                        )
                    )
                }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = uiState.value.searchMovies.collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = { query ->
                        uiState.value = uiState.value.copy(
                            query = query,
                            isSearching = true,
                            searchMovies = flow {
                                // Show all movies if query is empty or matches
                                if (query.isBlank() || fakeMovies.any { it.title?.contains(query, ignoreCase = true) == true }) {
                                    emit(PagingData.from(fakeMovies))
                                } else {
                                    emit(
                                        PagingData.empty(
                                            LoadStates(
                                                refresh = LoadState.Error(Throwable("No movies found!")),
                                                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                                                append = LoadState.NotLoading(endOfPaginationReached = true)
                                            )
                                        )
                                    )
                                }
                            }
                        )
                    },
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Perform text input
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("favorite")

        // Wait for UI update
        composeTestRule.waitForIdle()

        // Verify error UI is displayed
        composeTestRule.onNodeWithTag("ErrorScreen").assertExists()
        composeTestRule.onNodeWithText("we are sorry, we can \nnot find the movie :(").assertIsDisplayed()
    }

    @Test
    fun clickCancelButtonSearchShowsMoviesDisplay() {
        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember {
                    mutableStateOf(
                        SearchUiState(
                            trendingMovieUiState = TrendingMovieUiState(
                                trendingMovies = listOf(getFakeMovie(1, "Trending Movies 1")),
                                isTrendingMovie = false
                            ),
                            trendingTvUiState = TrendingTvUiState(
                                trendingTv = listOf(getFakeTvShow(1, "Trending Tv 1")),
                                isTrendingTv = false
                            ),
                            query = "",
                        )
                    )
                }

                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = uiState.value.searchMovies.collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = {},
                    onQueryChange = { query ->
                        uiState.value = uiState.value.copy(
                            query = query,
                            isSearching = query.isNotBlank(),
                            searchMovies = flow {
                                // Show all movies if query is empty or matches
                                if (query.isBlank() || fakeMovies.any { it.title?.contains(query, ignoreCase = true) == true }) {
                                    emit(PagingData.from(fakeMovies))
                                } else {
                                    emit(
                                        PagingData.empty(
                                            LoadStates(
                                                refresh = LoadState.Error(Throwable("No movies found!")),
                                                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                                                append = LoadState.NotLoading(endOfPaginationReached = true)
                                            )
                                        )
                                    )
                                }
                            }
                        )
                    },
                    snackBarMessageShown = {},
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Perform text input (case-insensitive search)
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("trending")
        // Wait for UI update
        composeTestRule.waitForIdle()
        // Verify SearchLazyColumn exists
        composeTestRule.onNodeWithTag("SearchLazyColumn").assertExists()
        // Check if searched movie appears
        composeTestRule.onNodeWithText("Trending Movie Search", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Cancel").assertExists().performClick()
        composeTestRule.onNodeWithTag("SuggestionsContentLazyColumn").assertIsDisplayed()
    }

    @Test
    fun clickSearchAddAndShowHistoryCard() {
        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember {
                    mutableStateOf(
                        SearchUiState(
                            trendingMovieUiState = TrendingMovieUiState(
                                trendingMovies = listOf(getFakeMovie(1, "Trending Movies 1")),
                                isTrendingMovie = false
                            ),
                            trendingTvUiState = TrendingTvUiState(
                                trendingTv = listOf(getFakeTvShow(1, "Trending Tv 1")),
                                isTrendingTv = false
                            ),
                            query = "",
                            historyUiState = HistoryUiState(
                                historyMovies = emptyList(),
                                isHistory = false
                            )
                        )
                    )
                }


                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = uiState.value.searchMovies.collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {
                        uiState.value = uiState.value.copy(
                            userMessage = mutableStateOf("Success Add to History"),
                            historyUiState = HistoryUiState(
                                historyMovies = listOf(getFakeMovie(1,"Trending Movie Search")),
                                isHistory = false
                            )
                        )
                    },
                    deleteFromHistory = {},
                    onQueryChange = { query ->
                        uiState.value = uiState.value.copy(
                            query = query,
                            isSearching = query.isNotBlank(),
                            searchMovies = flowOf(PagingData.from(fakeMovies)) // Ensure searchMovies updates with relevant results
                        )
                    },
                    snackBarMessageShown = {
                        uiState.value.userMessage.value = null
                    },
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        // Perform text input (case-insensitive search)
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("trending")
        // Wait for UI update
        composeTestRule.waitForIdle()
        // Verify SearchLazyColumn exists
        composeTestRule.onNodeWithTag("SearchLazyColumn").assertExists()
        // Check if searched movie appears
        composeTestRule.onNodeWithText("Trending Movie Search", ignoreCase = true).assertIsDisplayed().performClick()
        // Verify Snack bar is shown
        composeTestRule.onNodeWithText("Success Add to History").assertExists()
        composeTestRule.onNodeWithTag("Cancel").assertExists().performClick()
        composeTestRule.onNodeWithTag("SuggestionsContentLazyColumn").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MovieHorizontalCard1").assertIsDisplayed()

    }

    @Test
    fun clickDeleteHistoryCardAndShowSnackBar() {
        composeTestRule.setContent {
            MovieAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val uiState = remember {
                    mutableStateOf(
                        SearchUiState(
                            trendingMovieUiState = TrendingMovieUiState(
                                trendingMovies = listOf(getFakeMovie(1, "Trending Movies 1")),
                                isTrendingMovie = false
                            ),
                            trendingTvUiState = TrendingTvUiState(
                                trendingTv = listOf(getFakeTvShow(1, "Trending Tv 1")),
                                isTrendingTv = false
                            ),
                            query = "",
                            historyUiState = HistoryUiState(
                                historyMovies = listOf(getFakeMovie(1, "Trending Movie Search")), // Start with one movie
                                isHistory = true
                            )
                        )
                    )
                }


                SearchScreen(
                    uiState = uiState.value,
                    searchMovies = uiState.value.searchMovies.collectAsLazyPagingItems(),
                    onMovieClick = {},
                    onTvShowClick = {},
                    addToHistory = {},
                    deleteFromHistory = { movieToDelete ->
                        uiState.value = uiState.value.copy(
                            userMessage = mutableStateOf("Success Delete From History"),
                            historyUiState = uiState.value.historyUiState.copy(
                                historyMovies = uiState.value.historyUiState.historyMovies.filterNot { it.id == movieToDelete }
                            )
                        )
                    },
                    onQueryChange = { query ->
                        uiState.value = uiState.value.copy(
                            query = query,
                            isSearching = query.isNotBlank(),
                            searchMovies = flowOf(PagingData.from(fakeMovies)) // Ensure searchMovies updates with relevant results
                        )
                    },
                    snackBarMessageShown = {
                        uiState.value.userMessage.value = null
                    },
                    onSeeAllClick = {},
                    snackBarHostState = snackBarHostState
                )
            }
        }

        composeTestRule.onNodeWithTag("SuggestionsContentLazyColumn").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MovieHorizontalCard1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DeleteHistoryButton").assertExists().performClick()
        composeTestRule.onNodeWithText("Success Delete From History").assertExists()
    }

}