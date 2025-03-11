package com.example.movieapp.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.R
import com.example.movieapp.component.AuthCinemaxButton
import com.example.movieapp.component.AuthTopAppBar
import com.example.movieapp.component.CinemaxPasswordField
import com.example.movieapp.component.CinemaxTextField
import com.example.movieapp.component.LoaderScreen
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.White
import com.example.movieapp.ui.theme.WhiteGrey

@Composable
fun LoginRoute(
    onNavigateToForgotPassword : () -> Unit,
    onNavigateToHome : () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        onNavigateToHome = onNavigateToHome,
        onEmailChange = { viewModel.onEmailChange(it)},
        onPasswordChange = { viewModel.onPasswordChange(it)},
        onLoginClick = { viewModel.signInEmailAndPassword() },
        onForgotPasswordClick = onNavigateToForgotPassword,
        snackBarMessageShown = { viewModel.snackBarMessageShown() },
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onNavigateToHome: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick : () -> Unit,
    onForgotPasswordClick : () -> Unit,
    snackBarMessageShown : () -> Unit,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize().background(Dark))
    },
    snackBarHostState: SnackbarHostState
) {

    LaunchedEffect(uiState.isLogin) {
        if (uiState.isLogin) {
            onNavigateToHome()
        }
    }

    if(uiState.isLoading){
        loadingContent()
    }else {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                AuthTopAppBar(title = R.string.login, isAuthScreen = true, onBackButton = {})
            }
        ) {
            LoginContent(
                email = uiState.email,
                emailError = uiState.emailError,
                password = uiState.password,
                passwordError = uiState.passwordError,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onForgotPasswordClick = onForgotPasswordClick,
                isLogin = uiState.isLogin,
                modifier = Modifier.padding(it)
            )
        }
    }

    uiState.userMessage?.let { userMessage ->
       LaunchedEffect(userMessage) {
            snackBarHostState.showSnackbar(userMessage)
            snackBarMessageShown()
       }
    }
}

@Composable
fun LoginContent(
    email: String,
    emailError : String?,
    password : String,
    passwordError: String?,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick : () -> Unit,
    isLogin : Boolean,
    modifier : Modifier = Modifier
){
    Column(
        modifier
            .fillMaxSize()
            .background(Dark)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.hi_tiffany),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.welcome_back_please_enter_your_details),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = WhiteGrey
        )

        Spacer(modifier = Modifier.height(40.dp))

        CinemaxTextField(
            text = email,
            textError = emailError,
            labelName = R.string.email_address,
            onTextChange = onEmailChange)

        Spacer(modifier = Modifier.height(16.dp))

        CinemaxPasswordField(
            password = password,
            passwordError = passwordError,
            labelName = R.string.password,
            onPasswordChange = onPasswordChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.forgot_password),
            color = BlueAccent,
            modifier = Modifier.testTag("ForgotButton")
                .align(Alignment.End)
                .clickable { onForgotPasswordClick() }
        )
        Spacer(modifier = Modifier.height(32.dp))

        AuthCinemaxButton(
            title = R.string.login,
            onButtonClick = onLoginClick,
            email = email,
            password = password,
            loading = false,
            success = isLogin
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    MovieAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        Surface {
            LoginScreen(
                uiState = LoginUiState(
                    email = "atifafiorenza24@gmail.com",
                    emailError = null,
                    password = "12345678",
                    passwordError = null,
                    isLoading = false,
                    isLogin = false,
                    userMessage = null
                ),
                onNavigateToHome = {},
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onForgotPasswordClick = {},
                snackBarMessageShown = { /*TODO*/ },
                snackBarHostState = snackBarHostState
            )
        }
    }
}