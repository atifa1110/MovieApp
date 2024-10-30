package com.example.movieapp.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.movieapp.R
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.Movie
import com.example.movieapp.ui.theme.BlueAccent
import com.example.movieapp.ui.theme.White

@Composable
private fun <T> ContainerContent(
    items: List<T>,
    isLoading : Boolean,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
    placeholderContent: @Composable LazyItemScope.(Int) -> Unit,
    modifier: Modifier = Modifier,
    shouldShowPlaceholder: Boolean = items.isEmpty() && isLoading
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (shouldShowPlaceholder) {
            items(count = 10, itemContent = placeholderContent)
        } else {
            items(items = items, itemContent = itemContent)
        }
    }
}

@Composable
fun GenreListContainer(
    genres : List<String>,
    isLoading : Boolean,
    selectedGenre: String,
    onSelected : (String) -> Unit,
    modifier: Modifier = Modifier
){
    ContainerContent(
        modifier = modifier,
        items = genres,
        isLoading = isLoading,
        itemContent = { category ->
            MovieGenresCard(category = category,
            onSelected = onSelected , selectedCategory = selectedGenre)
        },
        placeholderContent = { MovieGenresShimmer() },
    )
}

@Composable
fun MovieVerticalListContainer(
    movies: List<Movie>,
    isLoading: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ContainerContent(
        modifier = modifier,
        items = movies,
        isLoading = isLoading,
        itemContent = { movie -> MovieVerticalCard(movie = movie, onClick = onClick) },
        placeholderContent = {  MovieShimmerVerticalCard() },
    )
}

@Composable
fun MoviesUpcomingPagingContainer(
    movies: LazyPagingItems<Movie>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = {
        ErrorMovie(
            errorImage = R.drawable.no_result,
            errorTitle = R.string.cannot_find_search,
            errorDescription = R.string.find_your_movie
        )
    },
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    errorContent: @Composable () -> Unit = {
        ErrorMovie(
            errorImage = R.drawable.no_result,
            errorTitle = R.string.cannot_find_search,
            errorDescription = R.string.find_your_movie
        )
    },
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(count = movies.itemCount, key = movies.itemKey { it.id ?: "" }) { item ->
            val movie = movies[item]
            UpcomingCard(movie = movie, onClick = onClick)
        }
    }

    when (movies.loadState.refresh){
        is LoadState.Loading -> {
            loadingContent()
        }
        is LoadState.Error -> {
            errorContent()
        }
        else -> {
            if(movies.itemSnapshotList.isEmpty()){
                emptyContent()
            }
        }
    }
}

@Composable
fun MoviesPagingContainer(
    movies: LazyPagingItems<Movie>,
    onClick: (Int) -> Unit,
    onHistory: (Movie) -> Unit ,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = {
        ErrorMovie(
            errorImage = R.drawable.no_result,
            errorTitle = R.string.cannot_find_search,
            errorDescription = R.string.find_your_movie
        )
    },
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    errorContent: @Composable () -> Unit = {
        ErrorMovie(
            errorImage = R.drawable.no_result,
            errorTitle = R.string.cannot_find_search,
            errorDescription = R.string.find_your_movie
        )
    }
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(count = movies.itemCount, key = movies.itemKey { it.id ?: "" }) { item ->
            val movie = movies[item]
            MovieHorizontalCard(movie = movie, onClick = onClick, onHistory = onHistory)
        }
    }

    when (movies.loadState.refresh){
        is LoadState.Loading -> {
            loadingContent()
        }
        is LoadState.Error -> {
            errorContent()
        }
        else -> {
            if(movies.itemSnapshotList.isEmpty()){
                emptyContent()
            }
        }
    }
}

@Composable
fun MoviesAndTvContainer(
    @StringRes titleResourceId: Int,
    onSeeAllClick: () -> Unit,
    movies: List<Movie>,
    isLoading : Boolean,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ContainerTitleWithButton(
            titleResourceId = titleResourceId,
            onSeeAllClick = onSeeAllClick
        )
        MovieVerticalListContainer(movies = movies, isLoading = isLoading,onClick = onMovieClick)
    }
}

@Composable
fun MoviesContainer(
    @StringRes titleResourceId: Int,
    onSeeAllClick: () -> Unit,
    content : @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(),
       horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainerTitleWithButton(titleResourceId = titleResourceId, onSeeAllClick = onSeeAllClick)
        Spacer(modifier = Modifier.padding(top = 8.dp))
        content()
    }
}

@Composable
fun ContainerTitleWithButton(
    @StringRes titleResourceId: Int,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = titleResourceId),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = stringResource(id = R.string.see_all),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BlueAccent
            )
        }
    }
}
