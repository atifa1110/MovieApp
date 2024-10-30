package com.example.movieapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.ui.theme.White

@Composable
fun GenreContainer(
    genres : List<String>,
    isLoading : Boolean,
    titleResourceId : Int,
    selectedGenre : String,
    onSelected : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = titleResourceId),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
        }
        GenreListContainer(
            genres = genres,
            isLoading = isLoading,
            selectedGenre = selectedGenre,
            onSelected = onSelected
        )
    }
}
