package com.example.movieapp.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MovieSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    filter: SectionFilter? = null,
    content: @Composable () -> Unit
) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            if (filter == null) {
                SectionTitle(title = title)
            } else {
                SectionTitle(
                    title = title,
                    modifier = modifier
                        .alignByBaseline()
                )
                SectionFilterButton(
                    filter = filter,
                    modifier = Modifier
                        .alignByBaseline()
                )
            }
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeSectionPreview(){

}

fun LazyListScope.homeSectionErrorText(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    item {
        ErrorText(
            title = title,
            modifier = modifier
        )
    }
}

fun LazyGridScope.homeSectionErrorText(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    item {
        ErrorText(
            title = title,
            modifier = modifier
        )
    }
}

@Composable
fun ErrorText(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = title),
        modifier = modifier.padding(vertical = 24.dp)
    )
}