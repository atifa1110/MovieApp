package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.onboarding.OnBoardingRoute

object OnBoardingDestination : CinemaxNavigationDestination {
    override val route: String = "boarding_route"
    override val destination: String = "boarding_destination"
}

fun NavGraphBuilder.boardingGraph(
    onNavigateToAuth : () -> Unit,
) = composable(OnBoardingDestination.route){
    OnBoardingRoute(onNavigateToAuth= onNavigateToAuth)
}