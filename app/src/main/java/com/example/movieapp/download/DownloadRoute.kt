package com.example.movieapp.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.movieapp.ui.theme.Dark

@Composable
fun DownloadRoute() {
    DownloadScreen()
}

@Composable
fun DownloadScreen(
    modifier : Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .background(Dark)) {

    }
}