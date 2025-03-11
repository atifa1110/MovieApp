package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.wishlist.presentation.WishlistRoute

object WishlistDestination : CinemaxNavigationDestination {
    override val route = "wishlist_route"
    override val destination = "wishlist_destination"
}

fun NavGraphBuilder.wishlistGraph(
) = composable(route = WishlistDestination.route) {
    WishlistRoute(onMovieClick = {})
}