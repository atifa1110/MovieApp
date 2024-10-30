package com.example.movieapp.password

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.auth.AlreadyHaveAccount
import com.example.movieapp.component.AuthTopAppBar
import com.example.movieapp.component.CinemaxButton
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Soft
import com.example.movieapp.ui.theme.White
import com.example.movieapp.ui.theme.WhiteGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen() {
    var code by remember { mutableStateOf("2") }

    Scaffold(
        topBar = {
            AuthTopAppBar(title = R.string.login, isAuthScreen = false, onBackButton = {})
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark)
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.verifying_your_account),
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))

            val initialText  = "We have just sent you 4 digit code via your email "
            val email = "example@gmail.com"

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Grey)) {
                    pushStringAnnotation(tag = initialText, annotation = initialText)
                    append(initialText)
                }
                withStyle(style = SpanStyle(color = White)) {
                    pushStringAnnotation(tag = email, annotation = email)
                    append(email)
                }
            }

            Text(
                text = annotatedString,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = WhiteGrey
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                repeat(4) { index ->
                    OutlinedTextField(
                        value = if (index < code.length) code[index].toString() else "",
                        onValueChange = {
                            code = it
                            if (it.length <= 1) {
                                code = code.take(index) + it + code.drop(index + 1)
                            }
                        },
                        textStyle = TextStyle(color = Color.White, fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,textAlign = TextAlign.Center),
                        singleLine = true,
                        modifier = Modifier.size(64.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedContainerColor = Soft,
                            unfocusedContainerColor = Soft,
                            focusedBorderColor = BlueAccent,
                            unfocusedBorderColor = Soft
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            CinemaxButton(
                title = R.string.continue_button,
                onButtonClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))

            AlreadyHaveAccount(onClick = { /*TODO*/ }, isVerification = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationPreview() {
    MovieAppTheme {
        Surface {
            VerificationScreen()
        }
    }
}