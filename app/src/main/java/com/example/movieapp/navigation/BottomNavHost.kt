package com.example.movieapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.movieapp.core.model.MediaType

@Composable
fun BottomNavHost(
    navController: NavHostController,
    startDestination: CinemaxNavigationDestination,
    onNavigateToDestination: (CinemaxNavigationDestination, String) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToAuth : () -> Unit,
    onNavigateToDetail: (MediaType.Details) -> Unit,
    onNavigateToList: (MediaType.Common) -> Unit,
    onNavigateToWishlist : () -> Unit,
    onNavigateToEditProfile : () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        homeGraph(
            onNavigateToWishlist = {
                onNavigateToWishlist()
            },
            onNavigateToListDestination = {
                onNavigateToList(it)
            },
            onNavigateToDetailsDestination = {
                onNavigateToDetail(it)
            }
        )
        searchGraph(
            onNavigateToDetailsDestination = {
                onNavigateToDetail(it)
            },
            onNavigateToListDestination = {
                onNavigateToList(it)
            },
        )
        downloadGraph()
        profileGraph(
            onNavigateToAuth = onNavigateToAuth ,
            onNavigateToEditProfile = onNavigateToEditProfile
        )
    }
}