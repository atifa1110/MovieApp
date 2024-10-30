package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.search.presentation.SearchRoute
import com.example.movieapp.wishlist.WishlistRoute

object WishlistDestination : CinemaxNavigationDestination {
    override val route = "wishlist_route"
    override val destination = "wishlist_destination"
}

fun NavGraphBuilder.wishlistGraph(
) = composable(route = WishlistDestination.route) {
    WishlistRoute(onMovieClick = {})
}