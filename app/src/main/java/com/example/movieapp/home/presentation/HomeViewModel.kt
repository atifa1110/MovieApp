package com.example.movieapp.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.model.TvShow
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.EventHandler
import com.example.movieapp.core.ui.asGenreNames
import com.example.movieapp.core.ui.asMediaTypeModel
import com.example.movieapp.core.ui.asMovie
import com.example.movieapp.core.ui.asTvShow
import com.example.movieapp.home.usecase.GetGenreMovieUseCase
import com.example.movieapp.home.usecase.GetTvShowUseCase
import com.example.movieapp.home.usecase.GetUserId
import com.example.movieapp.search.usecase.GetMovieUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val userId : FirebaseUser? = null,
    val genres : List<String> = emptyList(),
    val loadGenre : Boolean = false,
    val movies: Map<MediaType.Movie, List<Movie>> = emptyMap(),
    val tvShows: Map<MediaType.TvShow, List<TvShow>> = emptyMap(),
    val loadStates: Map<MediaType, Boolean> = emptyMap(),
    val errorMessage: String? = null,
    val isOfflineModeAvailable: Boolean = false,
    val selectedCategory : String = "Adventure"
)

internal val HomeUiState.isLoading: Boolean get() = loadStates.values.any { it }

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserId: GetUserId,
    private val getMovieUseCase: GetMovieUseCase,
    private val getGenreMovieUseCase: GetGenreMovieUseCase,
    private val getTvShowUseCase: GetTvShowUseCase
) : ViewModel(), EventHandler<HomeEvent> {

    private val _uiState = MutableStateFlow(HomeUiState(userId = getUserId()))
    val uiState = _uiState.asStateFlow()

    init {
        loadContent()
    }

    override fun onEvent(event: HomeEvent) = when (event) {
        HomeEvent.Refresh -> onRefresh()
        HomeEvent.Retry -> onRetry()
        HomeEvent.ClearError -> onClearError()
    }

    private fun loadContent() {
        val movieMediaTypes = listOf(
            MediaType.Movie.Upcoming,
            MediaType.Movie.NowPlaying,
            MediaType.Movie.Popular
        )
        movieMediaTypes.forEach(::loadMovies)

        val tvShowMediaTypes = listOf(
            MediaType.TvShow.Popular,
            MediaType.TvShow.NowPlaying
        )
        tvShowMediaTypes.forEach(::loadTvShow)

        loadGenre()
    }

    private fun onRefresh() {
        viewModelScope.coroutineContext.cancelChildren()
        loadContent()
    }

    private fun onRetry() {
        onClearError()
        onRefresh()
    }

    private fun onClearError() = _uiState.update { it.copy(errorMessage = null) }

    // Function to update the selected category
    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    private fun loadGenre() = viewModelScope.launch {
        getGenreMovieUseCase.invoke().collect{ response ->
            when(response){
                is CinemaxResponse.Loading -> {
                    _uiState.update {
                        it.copy(loadGenre = true)
                    }
                }
                is CinemaxResponse.Success -> {
                    val result = response.value.asGenreNames()
                    _uiState.update {
                        it.copy(loadGenre = false, genres = result)
                    }
                }
                is CinemaxResponse.Failure -> {
                    val errorResult = response.error
                    _uiState.update {
                        it.copy(loadGenre = false, errorMessage = errorResult)
                    }
                }
            }

        }
    }

    private fun loadMovies(mediaType: MediaType.Movie) = viewModelScope.launch {
        getMovieUseCase(mediaType.asMediaTypeModel()).collect{ response ->
            when(response){
                is CinemaxResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            loadStates = it.loadStates + (mediaType to true)
                        )
                    }
                }
                is CinemaxResponse.Success -> {
                    val data = response.value
                    _uiState.update {
                        it.copy(
                            movies = it.movies + (mediaType to data.map(MovieModel::asMovie)),
                            loadStates = it.loadStates + (mediaType to false)
                        )
                    }
                }
                is CinemaxResponse.Failure -> {
                    val error = response.error
                    handleFailure(error = error , mediaType = mediaType)
                }
            }
        }
    }

    private fun loadTvShow(mediaType: MediaType.TvShow) = viewModelScope.launch {
        getTvShowUseCase.invoke(mediaType.asMediaTypeModel()).collect{ response ->
            when(response){
                is CinemaxResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            loadStates = it.loadStates + (mediaType to true)
                        )
                    }
                }
                is CinemaxResponse.Success -> {
                    val data = response.value
                    _uiState.update {
                        it.copy(
                            tvShows = it.tvShows + (mediaType to data.map(TvShowModel::asTvShow)),
                            loadStates = it.loadStates + (mediaType to false)
                        )
                    }
                }
                is CinemaxResponse.Failure -> {
                    val error = response.error
                    handleFailure(error = error , mediaType = mediaType)
                }
            }
        }
    }

    private fun handleFailure(error: String, mediaType: MediaType) =
        _uiState.update {
            it.copy(
                errorMessage = error,
                isOfflineModeAvailable = it.movies.values.all(List<Movie>::isNotEmpty),
                loadStates = it.loadStates + (mediaType to false)
            )
        }
}