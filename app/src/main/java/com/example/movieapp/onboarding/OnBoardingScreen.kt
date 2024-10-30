package com.example.movieapp.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.R
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.White
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun OnBoardingRoute(
    onNavigateToAuth : () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
){

    OnBoardingScreen (
        setOnBoardingState = { viewModel.setOnBoardingState(true)},
        onNavigateToAuth = onNavigateToAuth
    )

}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    setOnBoardingState : () -> Unit,
    onNavigateToAuth : () -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Dark),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(page)
            }
        }

        Box (
            Modifier
                .fillMaxWidth()
                .padding(vertical = 56.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CustomPagerIndicator(pagerState = pagerState)
                IconButton(
                    onClick = {
                        if (pagerState.currentPage + 1 < pagerState.pageCount) {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        }else {
                            setOnBoardingState()
                            onNavigateToAuth()
                        }
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            BlueAccent,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Dark
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val indicatorWidth = 10.dp
    val indicatorSpacing = 16.dp
    val indicatorHeight = 10.dp
    val indicatorSelectedWidth = 32.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.width(100.dp)
    ) {
        repeat(pagerState.pageCount) { page ->
            val isSelected = pagerState.currentPage == page
            val width by animateFloatAsState(if (isSelected) indicatorSelectedWidth.value else indicatorWidth.value,
                label = ""
            )
            val color = if (isSelected) BlueAccent else Grey

            Box(
                modifier = Modifier
                    .width(width.dp)
                    .height(indicatorHeight)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
            if (page != pagerState.pageCount - 1) {
                Spacer(modifier = Modifier.width(indicatorSpacing))
            }
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    when (page) {
        0 -> OnboardingPageContent(
            image = R.drawable.boarding1,
            imageNumber = 0,
            title = "The biggest international and local film streaming",
            description = "Welcome to the ultimate movie streaming experience! Enjoy international blockbusters and local gems, from Hollywood hits to award-winning indie films and beloved classics. Our app has it all.",
        )
        1 -> OnboardingPageContent(
            image = R.drawable.boarding2,
            imageNumber = 1,
            title = "Offers ad-free viewing of high quality",
            description = "Semper in cursus magna et eu varius nunc adipiscing. Elementum justo, laoreet id sem semper parturient.",
        )
        2 -> OnboardingPageContent(
            image = R.drawable.boarding3,
            imageNumber = 2,
            title = "Our service brings to your favorite series",
            description = "Semper in cursus magna et eu varius nunc adipiscing. Elementum justo, laoreet id sem semper parturient.",
        )
    }
}

@Composable
fun OnboardingPageContent(
    @DrawableRes image : Int,
    imageNumber : Int,
    title: String,
    description: String
) {
    Box(Modifier.fillMaxSize()) {
        Image(
            modifier =
            if(imageNumber == 0) Modifier.fillMaxSize()
            else Modifier
                .fillMaxSize()
                .padding(32.dp),
            painter = painterResource(id = image),
            alignment = Alignment.TopCenter,
            contentDescription = "image"
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                backgroundColor = Color(0xFF171725),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(341.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.12.sp,
                        color = White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = description,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Grey,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPreview(){
    MovieAppTheme {
        Surface {
            OnBoardingScreen(
                setOnBoardingState ={},
                onNavigateToAuth= {}
            )
        }
    }
}