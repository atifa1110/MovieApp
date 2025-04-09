package com.example.movieapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.network.getGenreName
import com.example.movieapp.home.presentation.getFakeMovie
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.White

@Composable
fun UpcomingShimmerCard(){
    Column(
        Modifier
            .background(Dark)
            .padding(16.dp)){
        ShimmerEffect(
            Modifier
                .height(168.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)))

        Column(modifier = Modifier
            .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerEffect(
                Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            ShimmerEffect(
                Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun UpcomingCard(
    movie: Movie?,
    onClick : (Int) -> Unit,
    modifier : Modifier = Modifier
) {
    Column(
        modifier
            .background(Dark)
            .padding(16.dp)
            .clickable { onClick(movie?.id ?: 0) }
    ){
        Box(
            modifier
                .height(168.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie?.posterPath.toString())
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.poster_placeholder),
                contentDescription = movie?.posterPath.toString(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.poster_placeholder),
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.Black,
                    modifier = Modifier
                )
            }
        }

        Column(modifier = modifier
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = movie?.title.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Release Date",
                    tint = Grey,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movie?.releaseDate.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Grey
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "|",
                    color = Grey
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Genre",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = getGenreName(movie?.genres)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Grey
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UpcomingCardPreview() {
    MovieAppTheme {
        Surface {
            UpcomingCard(movie = getFakeMovie(), onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpcomingCardShimmerPreview() {
    MovieAppTheme {
        Surface {
            UpcomingShimmerCard()
        }
    }
}