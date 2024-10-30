package com.example.movieapp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.navigation.BottomNavHost
import com.example.movieapp.navigation.CinemaxNavHost
import com.example.movieapp.navigation.NavigationBottomBar
import com.example.movieapp.navigation.TopLevelDestination
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.MovieAppTheme

@Composable
fun MainScreen(
    onNavigateToAuth : () -> Unit,
    onNavigateToDetail : (MediaType.Details) -> Unit,
    onNavigateToList: (MediaType.Common) -> Unit,
    onNavigateToWishlist : () -> Unit,
    onNavigateToEditProfile : () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val topLevelDestinations = TopLevelDestination.values()
    val startDestination = TopLevelDestination.Home

    Scaffold (
        bottomBar = {
            NavigationBottomBar(
                items = topLevelDestinations,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(it)
        ) {
            BottomNavHost(
                navController = navController,
                startDestination = startDestination,
                onNavigateToDestination = { destination, route -> navController.navigate(route?:destination.route) },
                onBackClick = { navController.popBackStack() },
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToList = onNavigateToList,
                onNavigateToWishlist = onNavigateToWishlist,
                onNavigateToEditProfile = onNavigateToEditProfile
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MovieAppTheme {
        Surface {
            MainScreen(
                onNavigateToAuth = {},
                onNavigateToDetail = {},
                onNavigateToList = {},
                onNavigateToWishlist = {},
                onNavigateToEditProfile = {}
            )
        }
    }
}