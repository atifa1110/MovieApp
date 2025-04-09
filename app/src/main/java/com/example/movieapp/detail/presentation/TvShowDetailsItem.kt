package com.example.movieapp.detail.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.component.DetailTopAppBar
import com.example.movieapp.component.SeasonCard
import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.Season
import com.example.movieapp.core.model.TvShowDetails
import com.example.movieapp.core.network.getGenreName
import com.example.movieapp.core.network.getYearReleaseDate
import com.example.movieapp.home.presentation.getFakeDetailTvShow
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.MovieAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowDetailsItem(
    tvShow : TvShowDetails?,
    userMessage : Int?,
    scrollBehavior: TopAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    addToWishlist : () -> Unit,
    onBackButtonClick : () -> Unit,
    snackBarMessageShown : () -> Unit,
) {

    DetailsItemTvShow(
        title = tvShow?.name?:"",
        overview = tvShow?.overview?:"",
        posterPath = tvShow?.posterPath?:"",
        releaseDate = tvShow?.firstAirDate?:"",
        runtime = 0,
        genres = tvShow?.genres?: emptyList(),
        seasons = tvShow?.seasons?: emptyList(),
        rating = tvShow?.rating?:0.0,
        credits = tvShow?.credits,
        isWishlist = tvShow?.isWishListed?:false,
        userMessage = userMessage,
        onBackButtonClick = onBackButtonClick,
        addToWishlist = addToWishlist,
        snackBarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        snackBarMessageShown = snackBarMessageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsItemTvShow(
    title: String,
    overview: String,
    posterPath: String,
    releaseDate: String,
    runtime: Int,
    genres: List<Genre>,
    seasons : List<Season>,
    rating: Double,
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
    Box(
        modifier
            .fillMaxSize()
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
            LazyColumn(
                modifier.testTag("TvShowDetailsItemList")
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 16.dp, end= 16.dp, bottom = 16.dp)
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
                        colorDownload = BlueAccent,
                        colorButton = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                        textButton = " Trailer"
                    )
                }
                item{
                    DetailStoryLine(storyLine = overview)
                }
                item{
                    DetailCastAndCrew(credits = credits)
                }

                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.seasons),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                items(items = seasons){ item ->
                    SeasonCard(season = item)
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

@Composable
fun DetailSeason(
    season: List<Season>?
) {
    if(season?.isNotEmpty() == true){
        SeasonContainer(
            titleResourceId = R.string.seasons,
        ){
            items(season){item ->
                SeasonCard(season = item)
            }
        }
    }
}

@Composable
private fun SeasonContainer(
    @StringRes titleResourceId: Int,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = titleResourceId),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TvShowDetailScreenPreview() {
    MovieAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Surface {
            DetailContent(
                uiState = DetailsUiState(
                    mediaType = MediaType.Details.from(1,"tv_show"),
                    tvShow = getFakeDetailTvShow(),
                    isLoading = false,
                    userMessage = null
                ),
                snackBarHostState = snackBarHostState,
                scrollBehavior = scrollBehavior,
                onBackButtonClick = { /*TODO*/ },
                addToWishlist = { /*TODO*/ },
                snackBarMessageShown = { /*TODO*/ })
        }
    }
}
