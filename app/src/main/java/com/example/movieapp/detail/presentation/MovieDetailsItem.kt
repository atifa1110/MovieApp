package com.example.movieapp.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.component.DetailTopAppBar
import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.Images
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.network.getGenreName
import com.example.movieapp.core.network.getYearReleaseDate
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsItem (
    movie: MovieDetails?,
    userMessage : Int?,
    snackBarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackButtonClick: () -> Unit,
    addToWishlist : () -> Unit,
    snackBarMessageShown : () -> Unit,
){
    DetailsItem(
        title = movie?.title?:"",
        overview = movie?.overview?:"",
        posterPath = movie?.posterPath?:"",
        releaseDate = movie?.releaseDate?:"",
        runtime = movie?.runtime?:0,
        genres = movie?.genres?: emptyList(),
        rating = movie?.rating?:0.0,
        images = movie?.images,
        credits = movie?.credits,
        isWishlist = movie?.isWishListed?:false,
        userMessage = userMessage,
        onBackButtonClick = onBackButtonClick,
        addToWishlist = addToWishlist,
        snackBarMessageShown = snackBarMessageShown,
        snackBarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsItem(
    title: String,
    overview: String,
    posterPath: String,
    releaseDate: String,
    runtime: Int,
    genres: List<Genre>,
    rating: Double,
    images : Images?,
    credits: Credits?,
    isWishlist: Boolean,
    userMessage : Int?,
    onBackButtonClick: () -> Unit,
    addToWishlist: () -> Unit,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    snackBarMessageShown : () -> Unit,
    isPlaceholder: Boolean = false
) {
    Box(modifier.fillMaxSize()
        .background(Dark)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterPath)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.poster_placeholder),
            contentDescription = posterPath,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.poster_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(545.dp)
                .blur(radius = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1F1D2B).copy(alpha = 0.2f),
                            Color(0xFF1F1D2B).copy(alpha = 1.0f)
                        )
                    )
                )
        )

        Scaffold (
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                DetailTopAppBar(
                    title = title,
                    isWishlist = isWishlist,
                    onBackButtonClick = onBackButtonClick,
                    addToWishlist = addToWishlist,
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = Color.Transparent,
        ){
            LazyColumn(modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DetailPoster(poster = posterPath)
                }
                item{
                    DetailInfo(
                        releaseDate = releaseDate.getYearReleaseDate(),
                        runtime = runtime,
                        genre = getGenreName(genres),
                        rating = rating.toString()
                    )
                }
                item{
                    DetailActions(
                        colorDownload = Orange,
                        colorButton = ButtonDefaults.buttonColors(containerColor = Orange),
                        textButton = " Play"
                    )
                }
                item{
                    DetailStoryLine(storyLine = overview)
                }
                item{
                    DetailCastAndCrew(credits = credits)
                }
                item{
                    MovieGallery(images = images)
                }
            }
        }

        userMessage?.let { userMessage ->
            val snackBarText = stringResource(userMessage)
            LaunchedEffect(userMessage, snackBarText) {
                snackBarHostState.showSnackbar(snackBarText)
                snackBarMessageShown()
            }
        }
    }
}