package com.example.movieapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.movieapp.auth.AuthRoute

object AuthDestination : CinemaxNavigationDestination {
    override val route: String = "auth_route"
    override val destination: String = "auth_destination"
}

fun NavGraphBuilder.authGraph(
    onNavigateToLogin : () -> Unit,
    onNavigateToRegister : () -> Unit,
) = composable(route = AuthDestination.route) {
    AuthRoute(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToLogin = onNavigateToLogin
    )
}