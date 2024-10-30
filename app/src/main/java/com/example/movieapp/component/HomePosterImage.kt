package com.example.movieapp.component
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Icon
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.example.movieapp.R
//import com.example.movieapp.core.model.Movie
//import com.example.movieapp.ui.theme.MovieAppTheme
//
//@Composable
//fun HomePosterImage(
//    movie: Movie,
//    onDetailClick:()-> Unit,
//    onFavorite: (Movie) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(contentAlignment = Alignment.Center) {
//        Column(
//            modifier = modifier.width(150.dp),
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(200.dp).width(150.dp) // Replace with image
//            ) {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(movie.posterUrl)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = movie.title,
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.poster_placeholder),
//                    modifier = Modifier.fillMaxSize()
//                        .clip(shape = RoundedCornerShape(5.dp))
//                )
//                Icon(
//                    imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
//                    contentDescription = "Favorite",
//                    tint = if (movie.isFavorite) Color.Red else Color.Gray,
//                    modifier = Modifier.size(40.dp)
//                        .align(Alignment.BottomStart)
//                        .padding(8.dp).clickable {
//                            onFavorite(movie)
//                        }
//                )
//
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = movie.title,
//                maxLines = 1,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//            )
//            val rating = 4f
//            Row(
//                modifier = Modifier.padding(vertical = 4.dp)
//            ) {
//                for (i in 1..5) {
//                    Icon(
//                        imageVector = Icons.Filled.Star,
//                        contentDescription = "Star",
//                        tint = if (i <= rating) Color.Blue else Color.Gray,
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Preview("Light Mode", device = Devices.PIXEL_3)
//@Composable
//fun PosterFavoritePreview(){
//    MovieAppTheme {
//        Surface {
//            HomePosterImage(
//                movie = Movie(1, "Sonic the Frozen", "",true),
//                onDetailClick = {},
//                onFavorite = {}
//            )
//        }
//    }
//}
//
//@Preview("Light Mode", device = Devices.PIXEL_3)
//@Composable
//fun PosterUnFavoritePreview(){
//    MovieAppTheme {
//        Surface {
//            HomePosterImage(
//                movie = Movie(1, "Sonic the Frozen", "",false),
//                onDetailClick = {},
//                onFavorite = {}
//            )
//        }
//    }
//}
