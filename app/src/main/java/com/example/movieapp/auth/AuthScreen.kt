package com.example.movieapp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.component.AuthTopAppBar
import com.example.movieapp.component.CinemaxButton
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Soft
import com.example.movieapp.ui.theme.White
import com.example.movieapp.ui.theme.WhiteGrey

@Composable
fun AuthRoute(
    onNavigateToRegister : () -> Unit,
    onNavigateToLogin : () -> Unit
) {
    AuthScreen(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun AuthScreen(
    onNavigateToRegister : () -> Unit,
    onNavigateToLogin : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(88.dp),
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "Logo"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.cinemax),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = White
        )
        Text(
            text = stringResource(R.string.enter_your_registered_phone_number_to_sign_up),
            textAlign = TextAlign.Center,
            color = Grey
        )

        Spacer(modifier = Modifier.height(50.dp))

        CinemaxButton(
            title = R.string.sign_up,
            onButtonClick = {
                onNavigateToRegister()
            }
        )

        AlreadyHaveAccount(onClick = { onNavigateToLogin() }, isVerification = false)
        OrSignUpButton(onButtonClick = {})
        SocialMediaButton(onGoogleClick = {}, onAppleClick = {}, onFacebookClick = {})
    }
}

@Composable
fun AlreadyHaveAccount(
    onClick : () -> Unit,
    isVerification : Boolean
){
    val initialText = if(isVerification) stringResource(id = R.string.not_receive_code)else
        stringResource(R.string.already_have_account) + " "
    val login = if(isVerification) stringResource(id = R.string.resend) else stringResource(id = R.string.login)

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Grey)) {
            pushStringAnnotation(tag = initialText, annotation = initialText)
            append(initialText)
        }
        withStyle(style = SpanStyle(color = BlueAccent, fontWeight = FontWeight.SemiBold)) {
            pushStringAnnotation(tag = login, annotation = login)
            append(login)
        }
    }


    Text(modifier = Modifier
        .padding(top = 36.dp, bottom = 18.dp)
        .fillMaxWidth()
        .clickable { onClick() },
        text = annotatedString,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SocialMediaButton(
    onGoogleClick : () -> Unit,
    onAppleClick : () -> Unit,
    onFacebookClick : () -> Unit
){
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){


        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .size(69.dp)
                .background(White),
            onClick = onGoogleClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = stringResource(R.string.google)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .size(69.dp)
                .background(Color(0xFF4267B2)),
            onClick = onAppleClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.apple),
                contentDescription = stringResource(R.string.apple),
                tint = White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .size(69.dp)
                .background(Soft),
            onClick = onFacebookClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = stringResource(R.string.facebook),
                tint = White
            )
        }
    }
}

@Composable
fun OrSignUpButton(
    onButtonClick : () -> Unit
) {
    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(modifier = Modifier.width(50.dp),color = Soft)

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clickable { onButtonClick() },
            text = stringResource(id = R.string.or_sign_up_with),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Grey
        )

        Divider(modifier = Modifier.width(50.dp),color = Soft)

    }
}
@Preview(showBackground = true)
@Composable
fun AuthPreview(){
    MovieAppTheme {
        Surface {
            AuthScreen(
                onNavigateToLogin = {},
                onNavigateToRegister = {}
            )
        }
    }
}