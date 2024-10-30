package com.example.movieapp.search.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.R
import com.example.movieapp.component.GenreListContainer
import com.example.movieapp.component.SearchTextField
import com.example.movieapp.core.model.Movie
import com.example.movieapp.home.getFakeMovieListModel
import com.example.movieapp.component.MovieHorizontalSearchCard
import com.example.movieapp.component.MovieShimmerHorizontalCard
import com.example.movieapp.component.MoviesAndTvContainer
import com.example.movieapp.component.MoviesPagingContainer
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.home.getFakeMovieList
import com.example.movieapp.home.getFakeMovieList2Model
import com.example.movieapp.ui.theme.Dark
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SearchRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
){
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val searchMovies = uiState.searchMovies.collectAsLazyPagingItems()

    SearchScreen(
        uiState = uiState,
        searchMovies = searchMovies,
        onMovieClick = onMovieClick,
        snackBarMessageShown = {viewModel.snackBarMessageShown()},
        addToHistory = { movie -> viewModel.addSearchHistory(movie) },
        deleteFromHistory = { id -> viewModel.deleteSearchHistory(id) },
        onQueryChange = { query-> viewModel.onEvent(SearchEvent.ChangeQuery(query)) },
        onSelected = { category -> viewModel.onCategorySelected(category)},
        snackBarHostState = snackBarHostState
    )

}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    searchMovies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    addToHistory: (Movie) -> Unit,
    deleteFromHistory: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    snackBarMessageShown: () -> Unit,
    onSelected: (String) -> Unit,
    snackBarHostState: SnackbarHostState
) {

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ){
        SearchContent(
            uiState = uiState,
            searchMovies = searchMovies,
            onQueryChange = onQueryChange,
            onMovieClick = onMovieClick,
            addToHistory = addToHistory,
            deleteFromHistory = deleteFromHistory,
            onSelected = onSelected,
            modifier = Modifier.padding(it)
        )
    }
    uiState.userMessage.let { userMessage ->
        LaunchedEffect(userMessage) {
            snackBarHostState.showSnackbar(userMessage?:"")
            snackBarMessageShown()
        }
    }

}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    searchMovies : LazyPagingItems<Movie>,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    addToHistory: (Movie) -> Unit,
    deleteFromHistory: (Int) -> Unit,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .fillMaxSize()
        .background(Dark)
    ){
        SearchTextField(
            query = uiState.query,
            onQueryChange = onQueryChange
        )

        AnimatedContent(targetState = uiState.isSearching, label = "Searching") { isSearching ->
            if(isSearching){
                MoviesPagingContainer(movies = searchMovies, onClick = {}, onHistory = addToHistory)
            }else{
                SuggestionsContent(
                    searchHistory = uiState.historyUiState.historyMovies,
                    isHistory = uiState.historyUiState.isHistory,
                    movies = uiState.trendingUiState.trendingMovies,
                    isTrending = uiState.trendingUiState.isTrending,
                    genres =  uiState.genreUiState.genres,
                    isGenre = uiState.genreUiState.isGenres,
                    selectedGenre = uiState.genreUiState.selectedGenre,
                    onSelected = onSelected,
                    onSeeAllClick = {},
                    deleteFromHistory = deleteFromHistory,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}


@Composable
private fun SuggestionsContent(
    searchHistory: List<Movie>,
    isHistory : Boolean,
    movies: List<Movie>,
    isTrending : Boolean,
    genres : List<String>,
    isGenre : Boolean,
    selectedGenre : String,
    onSelected: (String) -> Unit,
    onSeeAllClick: (MediaType.Common) -> Unit,
    onMovieClick: (Int) -> Unit,
    deleteFromHistory: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldShowPlaceholder: Boolean = searchHistory.isEmpty() && isHistory
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Column(Modifier.fillMaxWidth()) {
                GenreListContainer(
                    genres = genres,
                    isLoading = isGenre,
                    selectedGenre = selectedGenre,
                    onSelected = {}
                )
            }
        }
        if(shouldShowPlaceholder){
            items(1){
                MovieShimmerHorizontalCard()
            }
        }else{
            items(searchHistory) { history ->
                    MovieHorizontalSearchCard(
                        movie = history,
                        onClick = {},
                        onDelete = deleteFromHistory
                    )
            }
        }

        item {
            MoviesAndTvContainer(
                titleResourceId = R.string.recommendation,
                onSeeAllClick = { onSeeAllClick(MediaType.Common.Trending) },
                movies = movies,
                isLoading = isTrending,
                onMovieClick = onMovieClick
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview(){
    //Mocked Pager flow
    val mockProducts = getFakeMovieListModel()

    val pager = Pager(PagingConfig(pageSize = 8)) {
        object : PagingSource<Int, Movie>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
                return LoadResult.Page(
                    data = mockProducts,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
        }
    }

    // Collect the paging data as LazyPagingItems
    val searchMovies = pager.flow.collectAsLazyPagingItems()

    val snackBarHostState = remember { SnackbarHostState() }
    MovieAppTheme {
       Surface {
           SearchScreen(
               uiState = SearchUiState(query = "Movie",isSearching = true),
               searchMovies = searchMovies,
               onMovieClick = {},
               addToHistory = {},
               deleteFromHistory = {},
               onQueryChange = {},
               snackBarMessageShown = {},
               onSelected = {},
               snackBarHostState = snackBarHostState
           )
       }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchRecommendedPreview(){
    //Mocked Pager flow
    val mockProducts = getFakeMovieListModel()

    val pager = Pager(PagingConfig(pageSize = 8)) {
        object : PagingSource<Int, Movie>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
                return LoadResult.Page(
                    data = mockProducts,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
        }
    }

    // Collect the paging data as LazyPagingItems
    val searchMovies = pager.flow.collectAsLazyPagingItems()

    val snackBarHostState = remember { SnackbarHostState() }
    MovieAppTheme {
        Surface {
            SearchScreen(
                uiState = SearchUiState(
                    query = "",
                    isSearching = false,
                    searchMovies =  emptyFlow(),
                    trendingUiState = TrendingUiState(getFakeMovieList(),false),
                    historyUiState = HistoryUiState(getFakeMovieList2Model(),false)
                ),
                searchMovies = searchMovies,
                onMovieClick = {},
                addToHistory = {},
                deleteFromHistory = {},
                onQueryChange = {},
                snackBarMessageShown = {},
                onSelected = {},
                snackBarHostState = snackBarHostState
            )
        }
    }
}