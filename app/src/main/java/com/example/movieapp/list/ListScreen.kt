package com.example.movieapp.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.example.movieapp.component.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.component.MoviesPagingContainer
import com.example.movieapp.component.MoviesUpcomingPagingContainer
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.Movie
import com.example.movieapp.ui.theme.Dark

@Composable
fun ListRoute(
    onUpcomingClick:(Int) -> Unit,
    onPopularNowClick:(Int) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ListScreen(
        uiState = uiState,
        onUpcomingClick = onUpcomingClick,
        onPopularNowClick = onPopularNowClick
    )
}
@Composable
fun ListScreen(
    uiState: ListUiState,
    onUpcomingClick: (Int) ->Unit,
    onPopularNowClick:(Int) -> Unit,
) {
    val movies = uiState.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(title = uiState.mediaType.asTitleResourceId()) {

            }
        }
    ) {
        ListContent(
            uiState = uiState,
            movies = movies,
            onUpcomingClick = onUpcomingClick,
            onPopularNowClick = onPopularNowClick,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun ListContent(
    uiState: ListUiState,
    movies: LazyPagingItems<Movie>,
    onUpcomingClick: (Int) -> Unit,
    onPopularNowClick:(Int) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(Dark)
    ) {
        when (uiState.mediaType) {
            MediaType.Common.Popular -> {
                MoviesPagingContainer(movies = movies, onClick = onPopularNowClick, onHistory = {})
            }

            MediaType.Common.Upcoming -> {
                MoviesUpcomingPagingContainer(movies = movies, onClick = onUpcomingClick)
            }

            MediaType.Common.NowPlaying -> {
                MoviesPagingContainer(movies = movies, onClick = onPopularNowClick, onHistory = {})
            }
            else -> TODO()
        }
    }
}
