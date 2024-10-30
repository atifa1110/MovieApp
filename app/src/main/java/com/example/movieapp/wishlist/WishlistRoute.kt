package com.example.movieapp.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.component.CinemaxSwipeRefresh
import com.example.movieapp.component.ErrorMovie
import com.example.movieapp.component.TopAppBar
import com.example.movieapp.component.WishlistCard
import com.example.movieapp.component.WishlistShimmer
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.model.WishList

@Composable
fun WishlistRoute(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    WishlistScreen(
        uiState = uiState,
        onRefreshMovies = {},
        onRetry = {},
        onOfflineModeClick = {},
        deleteFromWishlist = { id-> viewModel.deleteFromWishlist(id) },
        snackBarHostState = snackBarHostState,
        snackBarMessageShown = viewModel::snackBarMessageShown
    )
}

@Composable
fun WishlistScreen(
    uiState: WishlistUiState,
    onRefreshMovies: () -> Unit,
    onRetry: () -> Unit,
    onOfflineModeClick: () -> Unit,
    deleteFromWishlist: (Int) -> Unit,
    snackBarMessageShown: () -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(title = R.string.wishlist, onBackButton ={})
        }
    ){
        WishlistContent(
            movies = uiState.movies,
            isLoading = uiState.isLoading,
            onRefresh = onRefreshMovies,
            deleteFromWishlist = deleteFromWishlist,
            modifier = Modifier.padding(it)
        )
    }

    uiState.userMessage?.let { userMessage ->
        val snackBarText = stringResource(userMessage)
        LaunchedEffect(userMessage, snackBarText) {
            snackBarHostState.showSnackbar(snackBarText)
            snackBarMessageShown()
        }
    }

}

@Composable
fun WishlistContent(
    movies: List<WishList>,
    isLoading: Boolean,
    deleteFromWishlist: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = {
        ErrorMovie(errorImage = R.drawable.no_wishlist,
            errorTitle = R.string.cannot_find_movie,
            errorDescription = R.string.find_your_movie
        )
    },
    showPlaceholder : Boolean = isLoading
) {
    if (movies.isEmpty() && !isLoading) {
        emptyContent()
    }else {
        CinemaxSwipeRefresh(isRefreshing = isLoading, onRefresh = onRefresh) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                if (showPlaceholder) {
                    if (isLoading) {
                        items(5) {
                            WishlistShimmer()
                        }
                    }
                } else {
                    items(movies) { movieDetail ->
                        WishlistCard(
                            mediaType = "Movie",
                            movie = movieDetail,
                            deleteFromWishlist = deleteFromWishlist
                        )
                    }
                }
            }
        }
    }
}
