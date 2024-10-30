package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.splash.SplashRoute

object SplashDestination  : CinemaxNavigationDestination {
    override val route: String = "splash_route"
    override val destination: String = "splash_destination"
}

fun NavGraphBuilder.splashGraph(
    onNavigateToOnBoarding : () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
) = composable(route = SplashDestination.route) {
    SplashRoute(
        onNavigateToOnBoarding = onNavigateToOnBoarding,
        onNavigateToAuth = onNavigateToAuth,
        onNavigateToHome = onNavigateToHome
    )
}