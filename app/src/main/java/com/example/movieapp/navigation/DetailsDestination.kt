package com.example.movieapp.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.detail.presentation.DetailRoute

object DetailsDestination : CinemaxNavigationDestination {
    override val route = "details_route"
    override val destination = "details_destination"

    const val idArgument = "id"
    const val mediaTypeArgument = "mediaType"
    private const val MediaIdNullMessage = "Media id is null."
    private const val MediaTypeNullMessage = "Media type is null."

    val routeWithArguments = "$route/{$idArgument}/{$mediaTypeArgument}"

    fun createNavigationRoute(mediaType: MediaType.Details) =
        "$route/${mediaType.mediaId}/${mediaType.mediaType}"

    fun fromSavedStateHandle(savedStateHandle: SavedStateHandle) = MediaType.Details.from(
        id = checkNotNull(savedStateHandle[idArgument]) { MediaIdNullMessage },
        mediaType = checkNotNull(savedStateHandle[mediaTypeArgument]) { MediaTypeNullMessage }
    )
}

fun NavGraphBuilder.detailsGraph(
    onBackButtonClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) = composable(
    route = DetailsDestination.routeWithArguments,
    arguments = listOf(
        navArgument(DetailsDestination.idArgument) { type = NavType.IntType },
        navArgument(DetailsDestination.mediaTypeArgument) { type = NavType.StringType }
    )
) {
    DetailRoute(
        onBackButton = onBackButtonClick
    )
}