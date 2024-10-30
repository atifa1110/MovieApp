package com.example.movieapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.R
import com.example.movieapp.component.CinemaxSwipeRefresh
import com.example.movieapp.component.GenreContainer
import com.example.movieapp.component.HomeAppBar
import com.example.movieapp.component.MoviesAndTvContainer
import com.example.movieapp.component.SearchHome
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.Movie
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.MovieAppTheme

@Composable
fun HomeRoute(
    onSeeFavoriteClick: () -> Unit,
    onSeeAllClick: (MediaType.Common) -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel : HomeViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    HomeScreen(
        uiState = uiState,
        onSeeFavoriteClick = onSeeFavoriteClick,
        onSelected = { viewModel.onCategorySelected(it) },
        onSeeAllClick = onSeeAllClick,
        onMovieClick = onMovieClick,
        onRefresh = { viewModel.onEvent(HomeEvent.Refresh) },
        snackBarHostState = snackBarHostState
    )
}
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSelected: (String) -> Unit,
    onSeeFavoriteClick: () -> Unit,
    onSeeAllClick: (MediaType.Common) -> Unit,
    onMovieClick: (Int) -> Unit,
    onRefresh : () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            HomeAppBar(
                onSeeFavoriteClick = onSeeFavoriteClick,
                user = uiState.userId
            )
        },
        backgroundColor = Dark
    ) {
        CinemaxSwipeRefresh(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it)
        ) {
            HomeContent(
                movies = uiState.movies,
                loading = uiState.loadStates,
                genres = uiState.genres,
                loadGenre = uiState.loadGenre,
                selectedGenre = uiState.selectedCategory,
                onSelected = onSelected,
                onSeeAllClick = onSeeAllClick,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
fun HomeContent(
    movies: Map<MediaType.Movie, List<Movie>>,
    loading : Map<MediaType,Boolean>,
    genres : List<String>,
    loadGenre : Boolean,
    selectedGenre: String,
    onSelected: (String) -> Unit,
    onSeeAllClick: (MediaType.Common) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ){
        item{
            SearchHome(query = "", onQueryChange = {})
        }
        item{
            UpcomingMoviesContainer(
                movies = movies[MediaType.Movie.Upcoming].orEmpty(),
                isLoading = loading[MediaType.Movie.Upcoming]?:false,
                onSeeAllClick = { onSeeAllClick(MediaType.Common.Upcoming) },
                onMovieClick = onMovieClick
            )
        }
        item{
            GenreContainer(
                genres = genres,
                isLoading = loadGenre,
                titleResourceId = R.string.category,
                selectedGenre = selectedGenre,
                onSelected = onSelected,
            )
        }
        item{
            MoviesAndTvContainer(
                titleResourceId = R.string.most_popular,
                onSeeAllClick = { onSeeAllClick(MediaType.Common.Popular) },
                movies = movies[MediaType.Movie.Popular].orEmpty(),
                isLoading = loading[MediaType.Movie.Popular]?:false,
                onMovieClick = onMovieClick
            )
        }
        item{
            MoviesAndTvContainer(
                titleResourceId = R.string.now_playing,
                onSeeAllClick = { onSeeAllClick(MediaType.Common.NowPlaying)},
                movies = movies[MediaType.Movie.NowPlaying].orEmpty(),
                isLoading = loading[MediaType.Movie.NowPlaying]?:false,
                onMovieClick = onMovieClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeContentPreview(){
    MovieAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        // Sample Movie class for illustration

        //Assuming MediaType.Movie has different genres or categories
        val popularMediaType = MediaType.Movie.Popular
        val upcomingMediaType = MediaType.Movie.Upcoming

        // Fake map for movies
        val movies: Map<MediaType.Movie, List<Movie>> = mapOf(
            popularMediaType to listOf(getFakeMovie()),
            upcomingMediaType to listOf(getFakeMovie())
        )

        // Fake map for load states
        val loadStates: Map<MediaType, Boolean> = mapOf(
            popularMediaType to true, // Loaded
            upcomingMediaType to false // Not loaded
        )

        HomeScreen(
            uiState = HomeUiState(
                userId = null,
                movies = movies,
                genres = listOf("All","Action","Drama"),
                loadStates = loadStates,
                selectedCategory = "All",
                errorMessage = null,
                isOfflineModeAvailable = false
            ),
            onSelected = {},
            onSeeFavoriteClick = {},
            onSeeAllClick = {},
            onMovieClick = {},
            onRefresh = {},
            snackBarHostState = snackBarHostState
        )
    }
}