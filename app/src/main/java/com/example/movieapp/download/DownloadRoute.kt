package com.example.movieapp.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.movieapp.R
import com.example.movieapp.component.ErrorMovie
import com.example.movieapp.ui.theme.Dark

@Composable
fun DownloadRoute() {
    DownloadScreen()
}

@Composable
fun DownloadScreen(
    modifier : Modifier = Modifier
) {
    Column(modifier.fillMaxSize().background(Dark),
        verticalArrangement = Arrangement.Center
    ) {
        ErrorMovie(errorImage = R.drawable.no_wishlist,
            errorTitle = R.string.coming_soon,
            errorDescription = R.string.found_out_soon
        )
    }
}