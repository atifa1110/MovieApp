package com.example.movieapp.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.movieapp.R
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Pink80
import com.example.movieapp.ui.theme.Purple40
import com.example.movieapp.ui.theme.WhiteGrey

@Composable
fun ErrorScreen(
    onTryAgain : () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val animationLottie by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(resId = R.raw.animation)
        )
        val animationAction by animateLottieCompositionAsState(
            composition = animationLottie,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
            composition = animationLottie,
            progress = { animationAction }
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Check your connection or try again later"
        )

        Button(
            onClick = onTryAgain,
            modifier = Modifier.padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink80,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.ok_button_text),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun ErrorMovie(
    @DrawableRes errorImage: Int,
    @StringRes errorTitle: Int,
    @StringRes errorDescription: Int,
){
    Column(modifier = Modifier.fillMaxSize().background(Dark)
        .testTag("ErrorScreen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = errorImage),
            contentDescription = "No Result")
        Text(modifier = Modifier.padding(top = 16.dp).testTag(stringResource(id = errorTitle)),
            text = stringResource(id = errorTitle),
            color= WhiteGrey,textAlign = TextAlign.Center)
        Text(modifier = Modifier.padding(16.dp),
            text = stringResource(id = errorDescription),
            color = Grey, textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun ErrorScreenPreview(){
    MovieAppTheme{
        Surface {
            ErrorMovie(errorImage = R.drawable.no_result,
                errorTitle = R.string.cannot_find_search,
                errorDescription = R.string.find_your_movie
            )
        }
    }
}

@Preview
@Composable
fun ErrorDownloadPreview(){
    MovieAppTheme{
        Surface {
            ErrorMovie(errorImage = R.drawable.no_movie,
                errorTitle = R.string.cannot_find_movie,
                errorDescription = R.string.find_your_movie
            )
        }
    }
}

@Preview
@Composable
fun ErrorWishlistPreview(){
    MovieAppTheme{
        Surface {
            ErrorMovie(errorImage = R.drawable.no_wishlist,
                errorTitle = R.string.cannot_find_movie,
                errorDescription = R.string.find_your_movie
            )
        }
    }
}