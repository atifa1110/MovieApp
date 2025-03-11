package com.example.movieapp.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.Grey
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.Soft
import com.example.movieapp.ui.theme.White
import com.example.movieapp.ui.theme.WhiteGrey

@Composable
fun CinemaxButton(
    @StringRes title : Int,
    onButtonClick : () -> Unit,
) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueAccent,
            disabledContainerColor = Soft,
            contentColor = White,
            disabledContentColor = Grey
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape
    ) {
        Text(text = stringResource(id = title), color = White)
    }
}

@Composable
fun AuthCinemaxButton(
    @StringRes title : Int,
    onButtonClick : () -> Unit,
    email: String,
    password : String,
    loading: Boolean,
    success: Boolean
) {
    val enterAnimation = fadeIn(
        animationSpec = tween(
            durationMillis = 1000,
        )
    ) + slideInVertically(
        initialOffsetY = { 120.dp.value.toInt() },
        animationSpec = tween(durationMillis = 1000)
    )

    val exitAnimation = fadeOut() + slideOutVertically { -80.dp.value.toInt() }

    Button(
        enabled = email.isNotEmpty() && password.isNotEmpty(),
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueAccent,
            disabledContainerColor = Soft,
            contentColor = White,
            disabledContentColor = Grey
        ),
        modifier = Modifier.testTag("AuthButton")
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape
    ) {
        Text(text = stringResource(id = title))
    }
}


@Preview(showBackground = true)
@Composable
fun LoginButtonPreview(){
    MovieAppTheme {
        Surface {
            AuthCinemaxButton(title = R.string.login, onButtonClick = {},
                email = "", password = "", loading = true,success = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterButtonPreview(){
    MovieAppTheme {
        Surface {
            CinemaxButton(title = R.string.sign_up, onButtonClick = {})
        }
    }
}