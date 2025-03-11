package com.example.movieapp.wishlist.presentation

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
import androidx.compose.ui.platform.testTag
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
import com.example.movieapp.core.model.WishList

@Composable
fun WishlistRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    WishlistScreen(
        uiState = uiState,
        onRefreshMovies = {},
        onMovieClick = onMovieClick,
        deleteMovieFromWishlist = { id-> viewModel.deleteMovieFromWishlist(id) },
        deleteTvShowFromWishlist = { id-> viewModel.deleteTvShowFromWishlist(id)},
        snackBarHostState = snackBarHostState,
        snackBarMessageShown = viewModel::snackBarMessageShown
    )
}

@Composable
fun WishlistScreen(
    uiState: WishlistUiState,
    onRefreshMovies: () -> Unit,
    onMovieClick: (Int) -> Unit,
    deleteMovieFromWishlist: (Int) -> Unit,
    deleteTvShowFromWishlist: (Int) -> Unit,
    snackBarMessageShown: () -> Unit,
    snackBarHostState: SnackbarHostState,
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
            wishlist = uiState.wishlist,
            isLoading = uiState.isLoading,
            onRefresh = onRefreshMovies,
            onMovieClick = onMovieClick,
            deleteMovieFromWishlist = deleteMovieFromWishlist,
            deleteTvShowFromWishlist = deleteTvShowFromWishlist,
            modifier = Modifier.padding(it)
        )
    }

    uiState.userMessage.value?.let { userMessage ->
        val snackBarText = stringResource(userMessage)
        LaunchedEffect(snackBarText) {
            snackBarHostState.showSnackbar(snackBarText)
            snackBarMessageShown()
        }
    }

}

@Composable
fun WishlistContent(
    wishlist: List<WishList>,
    isLoading: Boolean,
    deleteMovieFromWishlist: (Int) -> Unit,
    deleteTvShowFromWishlist: (Int) -> Unit,
    onRefresh: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = {
        ErrorMovie(errorImage = R.drawable.no_wishlist,
            errorTitle = R.string.cannot_find_movie,
            errorDescription = R.string.find_your_movie
        )
    },
    showPlaceholder : Boolean = isLoading
) {
    if (wishlist.isEmpty() && !isLoading) {
        emptyContent()
    }else {
        CinemaxSwipeRefresh(isRefreshing = isLoading, onRefresh = onRefresh) {
            LazyColumn(
                modifier = modifier.fillMaxSize().testTag("WishlistList"),
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
                    items(wishlist, key = {it.id}) { list ->
                        WishlistCard(
                            mediaType = list.mediaType.name,
                            movie = list,
                            onClick = onMovieClick,
                            deleteFromWishlist = {
                                if(list.mediaType.name == "Movie"){
                                    deleteMovieFromWishlist(it)
                                } else{
                                    deleteTvShowFromWishlist(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
