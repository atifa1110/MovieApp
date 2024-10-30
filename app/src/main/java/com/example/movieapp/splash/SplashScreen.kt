package com.example.movieapp.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.R
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome : () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    val onBoarding by viewModel.onBoardingState.collectAsStateWithLifecycle(initialValue = false)
    val onLogin by viewModel.onLoginState.collectAsStateWithLifecycle(initialValue = false)

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        ), label = ""
    )

    // splash screen loading check if boarding state and login state exist or not
    // if splash screen finish than go to screen that state complete
    LaunchedEffect(viewModel) {
        startAnimation = true
        delay(3000)
        when{
            !onBoarding -> onNavigateToOnBoarding()
            !onLogin -> onNavigateToAuth()
            else -> onNavigateToHome()
        }
    }

    SplashScreen(alpha = alphaAnim.value)
}

@Composable
fun SplashScreen(alpha : Float) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Dark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Image(
            modifier = Modifier
                .size(138.dp, 138.dp)
                .alpha(alpha = alpha),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview(){
    MovieAppTheme {
        Surface {
            SplashScreen(alpha = 1f)
        }
    }
}