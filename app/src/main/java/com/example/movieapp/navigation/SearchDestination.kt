package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.search.presentation.SearchRoute

object SearchDestination : CinemaxNavigationDestination {
    override val route = "search_route"
    override val destination = "search_destination"
}

fun NavGraphBuilder.searchGraph(
    onNavigateToDetailsDestination: (MediaType.Details) -> Unit,
) = composable(route = SearchDestination.route) {
    SearchRoute(
        onMovieClick = { onNavigateToDetailsDestination(MediaType.Details.Movie(it)) }
    )
}