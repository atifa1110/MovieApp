package com.example.movieapp.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.movieapp.R
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.network.getGenreName
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.White
import com.example.movieapp.component.DetailTopAppBar
import com.example.movieapp.core.model.Videos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailersDetailsItem(
    movie : MovieDetails?,
    userMessage : Int?,
    scrollBehavior: TopAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    addToWishlist : () -> Unit,
    onBackButtonClick : () -> Unit,
    snackBarMessageShown : () -> Unit,
) {
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            DetailTopAppBar(
                title = stringResource(R.string.trailer),
                isWishlist = movie?.isWishListed?:false,
                containerColor = Dark,
                scrollBehavior = scrollBehavior,
                onBackButtonClick = onBackButtonClick,
                addToWishlist = addToWishlist
            )
        },
        containerColor = Dark,
    ){
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item{
                VideoPlayer(video = movie?.videos)
            }
            item{
                Column {
                    Text(text = movie?.title?:"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "", tint = Grey
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = movie?.releaseDate ?: "",
                            color = Grey,
                            fontSize = 14.sp
                        )

                        Text(text = "|", color = Grey, modifier = Modifier.padding(end = 8.dp))

                        Icon(painter = painterResource(id = R.drawable.film),
                            contentDescription = "",tint = Grey)
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = stringResource(id = getGenreName(movie?.genres)),
                            color = Grey,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            item{
                DetailStoryLine(storyLine = movie?.overview?:"")
            }
            item{
                DetailCastAndCrew(credits = movie?.credits)
            }
            item{
                MovieGallery(images = movie?.images)
            }
        }
    }

    userMessage?.let { message ->
        val snackBarText = stringResource(message)
        LaunchedEffect(message, snackBarText) {
            snackBarHostState.showSnackbar(snackBarText)
            snackBarMessageShown()
        }
    }

}


@Composable
fun VideoPlayer(video : Videos?){
    val result = video?.results?.filter { it.site == "YouTube" && it.type == "Teaser" }?: emptyList()
    val key = result[0].key

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)) {

        YouTubePlayerComposable(
            videoId = key,
            modifier = Modifier.testTag("VideoPlayer")
                .fillMaxWidth()
                .height(180.dp).clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun YouTubePlayerComposable(videoId: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    //Remember the YouTubePlayerView instance to manage its lifecycle properly
    val youTubePlayerView = remember { YouTubePlayerView(context) }

    DisposableEffect(Unit) {
        // Add a listener to the YouTubePlayerView when it's ready
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // Load the YouTube video using the video ID
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })

        onDispose {
            // Release the YouTube player when the composable leaves the composition
            youTubePlayerView.release()
        }
    }

    // Use AndroidView to integrate the traditional YouTubePlayerView within Compose
    AndroidView(
        factory = { youTubePlayerView },
        modifier = modifier
    )
}