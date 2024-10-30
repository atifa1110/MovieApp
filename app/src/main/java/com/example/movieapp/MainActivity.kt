package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.login.presentation.LoginScreen
import com.example.movieapp.main.MainScreen
import com.example.movieapp.navigation.CinemaxNavHost
import com.example.movieapp.navigation.SplashDestination
import com.example.movieapp.register.RegisterRoute
import com.example.movieapp.register.RegisterScreen
import com.example.movieapp.search.presentation.SearchRoute
import com.example.movieapp.search.presentation.SearchScreen
import com.example.movieapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CinemaxNavHost(
                        navController = rememberNavController(),
                        startDestination = SplashDestination,
                        onBackButtonClick = { /*TODO*/ },
                        onShowMessage = {}
                    )
                }
            }
        }
    }
}
