package com.example.movieapp.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.list.ListRoute
import com.example.movieapp.list.ListScreen

object ListDestination : CinemaxNavigationDestination {
    override val route = "list_route"
    override val destination = "list_destination"

    const val mediaTypeArgument = "mediaType"
    val routeWithArgument = "$route/{$mediaTypeArgument}"

    fun createNavigationRoute(mediaType: MediaType.Common) = "$route/${mediaType.mediaType}"

    fun fromSavedStateHandle(savedStateHandle: SavedStateHandle) = MediaType.Common[
        checkNotNull(savedStateHandle[mediaTypeArgument]) { MediaTypeNullMessage }
    ]
}

fun NavGraphBuilder.listGraph(
    onNavigateToDetailsDestination: (MediaType.Details) -> Unit,
) = composable(
    route = ListDestination.routeWithArgument,
    arguments = listOf(
        navArgument(ListDestination.mediaTypeArgument) { type = NavType.StringType }
    )
) {
    ListRoute(
        onUpcomingClick = { onNavigateToDetailsDestination(MediaType.Details.Trailers(it)) },
        onPopularNowClick = { onNavigateToDetailsDestination(MediaType.Details.Movie(it)) }
    )
}

private const val MediaTypeNullMessage = "Media type is null."