package com.example.movieapp.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.component.AuthTopAppBar
import com.example.movieapp.component.CinemaxButton
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Soft
import com.example.movieapp.ui.theme.White
import com.example.movieapp.ui.theme.WhiteGrey

@Composable
fun ResetScreen(

) {
    var email by remember { mutableStateOf("Tiffanyjearsey@gmail.com") }

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
                text = stringResource(R.string.reset_password),
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.recover_your_account_password),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = WhiteGrey
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_address), color = White) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Grey,
                    focusedLabelColor = White,
                    unfocusedTextColor = Grey,
                    unfocusedLabelColor = White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedBorderColor = Soft,
                    unfocusedBorderColor = Soft
                )
            )

            Spacer(modifier = Modifier.height(32.dp))
            CinemaxButton(title = R.string.next, onButtonClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPreview(){
    MovieAppTheme {
        Surface {
            ResetScreen()
        }
    }
}