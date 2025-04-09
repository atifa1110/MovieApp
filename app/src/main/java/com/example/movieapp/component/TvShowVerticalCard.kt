package com.example.movieapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.core.model.TvShow
import com.example.movieapp.core.network.getGenreName
import com.example.movieapp.home.presentation.getFakeTvShow
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Orange
import com.example.movieapp.ui.theme.Soft
import com.example.movieapp.ui.theme.Star
import com.example.movieapp.ui.theme.White

@Composable
fun TvShowVerticalCard(
    tvShow: TvShow?,
    onClick : (Int) -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(135.dp, 231.dp)
            .clickable {
                onClick(tvShow?.id ?: 0)
            },
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Soft
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .width(135.dp)
                    .height(170.dp) // Replace with image
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tvShow?.posterPath.toString())
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.poster_placeholder),
                    contentDescription = tvShow?.posterPath.toString(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.poster_placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Star)
                            .padding(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Orange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = tvShow?.rating.toString(),
                                fontSize = 12.sp,
                                color = Orange,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = tvShow?.name.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = White
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = getGenreName(tvShow?.genres)),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Grey
                )
            }
        }
    }
}

@Composable
fun TvShowShimmerVerticalCard() {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(Soft)
        .size(135.dp, 231.dp)
    ) {
        Column {
            ShimmerEffect(
                Modifier
                    .width(135.dp)
                    .height(170.dp)
                    .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
            )

            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ShimmerEffect(
                    Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(10.dp)))
                ShimmerEffect(
                    Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(10.dp)))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TvShowVerticalPreview() {
    MovieAppTheme {
        TvShowVerticalCard(
            tvShow = getFakeTvShow(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TvShowShimmerVerticalPreview() {
    MovieAppTheme {
        Surface {
            TvShowShimmerVerticalCard()
        }
    }
}