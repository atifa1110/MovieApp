package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.home.HomeRoute

object HomeDestination : CinemaxNavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    onNavigateToWishlist : () -> Unit,
    onNavigateToListDestination: (MediaType.Common) -> Unit,
    onNavigateToDetailsDestination: (MediaType.Details) -> Unit,
) = composable(route = HomeDestination.route) {
    HomeRoute(
        onSeeFavoriteClick = onNavigateToWishlist,
        onSeeAllClick = { onNavigateToListDestination(it) },
        onMovieClick = { onNavigateToDetailsDestination ( MediaType.Details.Movie(it))}
    )
}