package com.example.movieapp.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.White

@Composable
fun MovieCastCrewCard(
    profilePath: String?,
    name: String,
    description: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically){
        Box(modifier = Modifier.size(45.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profilePath)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.poster_placeholder),
                contentDescription = profilePath,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.poster_placeholder),
                modifier = Modifier.clip(CircleShape)
            )
        }
        Column(Modifier.padding(horizontal = 10.dp)){
            Text(
                text = name,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier.padding(vertical = 2.dp),
                text = description,
                color = Grey,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieCardPreview(){
    MovieAppTheme{
        MovieCastCrewCard(profilePath = "", name = "John Watt", description = "")
    }
}