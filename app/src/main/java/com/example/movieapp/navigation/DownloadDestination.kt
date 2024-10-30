package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.download.DownloadRoute

object DownloadDestination : CinemaxNavigationDestination {
    override val route = "download_route"
    override val destination = "download_destination"
}

fun NavGraphBuilder.downloadGraph(
) = composable(route = DownloadDestination.route) {
    DownloadRoute()
}