package com.example.movieapp.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.White
import java.util.Locale


@Composable
fun SectionTitle(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = title),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = White,
        modifier = modifier
            .paddingFromBaseline(top = 20.dp, bottom = 10.dp)
            .padding(start = 16.dp)
    )
}

@Composable
fun SectionFilterButton(
    filter: SectionFilter,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = filter.text),
        color = BlueAccent,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .padding(end = 16.dp)
            .clickable { filter.onClick() }
    )
}

data class SectionFilter(
    @StringRes val text: Int = R.string.section_filter_text_default,
    val onClick: () -> Unit
)