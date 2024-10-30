package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.profile.EditProfileRoute

object EditProfileDestination : CinemaxNavigationDestination {
    override val route = "edit_profile_route"
    override val destination = "edit_profile_destination"
}

fun NavGraphBuilder.editProfileGraph(
) = composable(route = EditProfileDestination.route) {
    EditProfileRoute()
}
